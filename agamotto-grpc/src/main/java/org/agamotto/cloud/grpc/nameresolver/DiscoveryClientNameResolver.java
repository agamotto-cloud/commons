

package org.agamotto.cloud.grpc.nameresolver;

import com.google.common.collect.Lists;
import io.grpc.*;
import io.grpc.internal.SharedResourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;


@Slf4j
public class DiscoveryClientNameResolver extends NameResolver {


    private static final String LEGACY_CLOUD_DISCOVERY_METADATA_PORT = "gRPC.port";
    private static final List<ServiceInstance> KEEP_PREVIOUS = null;

    private final String name;
    private final DiscoveryClient client;
    private final SynchronizationContext syncContext;
    private final Runnable externalCleaner;
    private final SharedResourceHolder.Resource<Executor> executorResource;
    private final boolean usingExecutorResource;


    private Listener2 listener;

    private Executor executor;
    private boolean resolving;
    private List<ServiceInstance> instanceList = Lists.newArrayList();


    public DiscoveryClientNameResolver(final String name, final DiscoveryClient client, final Args args,
                                       final SharedResourceHolder.Resource<Executor> executorResource, final Runnable externalCleaner) {
        this.name = name;
        this.client = client;
        this.syncContext = requireNonNull(args.getSynchronizationContext(), "syncContext");
        this.externalCleaner = externalCleaner;
        this.executor = args.getOffloadExecutor();
        this.usingExecutorResource = this.executor == null;
        this.executorResource = executorResource;
    }

    @Override
    public final String getServiceAuthority() {
        return this.name;
    }

    @Override
    public void start(final Listener2 listener) {
        checkState(this.listener == null, "already started");
        if (this.usingExecutorResource) {
            this.executor = SharedResourceHolder.get(this.executorResource);
        }
        this.listener = checkNotNull(listener, "listener");
        resolve();
    }

    @Override
    public void refresh() {
        checkState(this.listener != null, "not started");
        resolve();
    }


    public void refreshFromExternal() {
        this.syncContext.execute(() -> {
            if (this.listener != null) {
                resolve();
            }
        });
    }

    private void resolve() {
        log.debug("Scheduled resolve for {}", this.name);
        if (this.resolving) {
            return;
        }
        this.resolving = true;
        this.executor.execute(new Resolve(this.listener, this.instanceList));
    }

    @Override
    public void shutdown() {
        this.listener = null;
        if (this.executor != null && this.usingExecutorResource) {
            this.executor = SharedResourceHolder.release(this.executorResource, this.executor);
        }
        this.instanceList = Lists.newArrayList();
        if (this.externalCleaner != null) {
            this.externalCleaner.run();
        }
    }

    @Override
    public String toString() {
        return "DiscoveryClientNameResolver [name=" + this.name + ", discoveryClient=" + this.client + "]";
    }


    private final class Resolve implements Runnable {

        private final Listener2 savedListener;
        private final List<ServiceInstance> savedInstanceList;


        Resolve(final Listener2 listener, final List<ServiceInstance> instanceList) {
            this.savedListener = requireNonNull(listener, "listener");
            this.savedInstanceList = requireNonNull(instanceList, "instanceList");
        }

        @Override
        public void run() {
            final AtomicReference<List<ServiceInstance>> resultContainer = new AtomicReference<>();
            try {
                resultContainer.set(resolveInternal());
            } catch (final Exception e) {
                this.savedListener.onError(Status.UNAVAILABLE.withCause(e)
                        .withDescription("更新服务列表出错: " + DiscoveryClientNameResolver.this.name));
                resultContainer.set(Lists.newArrayList());
            } finally {
                DiscoveryClientNameResolver.this.syncContext.execute(() -> {
                    DiscoveryClientNameResolver.this.resolving = false;
                    final List<ServiceInstance> result = resultContainer.get();
                    if (result != KEEP_PREVIOUS && DiscoveryClientNameResolver.this.listener != null) {
                        DiscoveryClientNameResolver.this.instanceList = result;
                    }
                });
            }
        }


        private List<ServiceInstance> resolveInternal() {
            final String name = DiscoveryClientNameResolver.this.name;
            final List<ServiceInstance> newInstanceList =
                    DiscoveryClientNameResolver.this.client.getInstances(name);
            if (log.isDebugEnabled()) {
                log.debug("获取到服务{}实例{}个", name, newInstanceList.size());
            }
            if (CollectionUtils.isEmpty(newInstanceList)) {
                log.error("未找到服务 {} 的实例", name);
                this.savedListener.onError(Status.UNAVAILABLE.withDescription("未找到服务 " + name));
                return Lists.newArrayList();
            }
            if (!needsToUpdateConnections(newInstanceList)) {
                if (log.isDebugEnabled()) {
                    log.debug("服务列表无变化，不需要变更{}", name);
                }
                return KEEP_PREVIOUS;
            }

            final List<EquivalentAddressGroup> targets = Lists.newArrayList();
            for (final ServiceInstance instance : newInstanceList) {
                final int port = getGRPCPort(instance);
                targets.add(new EquivalentAddressGroup(
                        new InetSocketAddress(instance.getHost(), port), Attributes.EMPTY));
            }
            if (targets.isEmpty()) {
                log.error("服务{}出现了异常", name);
                this.savedListener.onError(Status.UNAVAILABLE
                        .withDescription("未找到服务" + name + "实例"));
                return Lists.newArrayList();
            } else {
                this.savedListener.onResult(ResolutionResult.newBuilder()
                        .setAddresses(targets)
                        .build());
                return newInstanceList;
            }
        }


        private int getGRPCPort(final ServiceInstance instance) {
            final Map<String, String> metadata = instance.getMetadata();
            if (metadata == null) {
                return instance.getPort() + 1;
            }
            String portString = metadata.get(LEGACY_CLOUD_DISCOVERY_METADATA_PORT);
            if (portString == null) {
                return instance.getPort() + 1;
            }
            try {
                return Integer.parseInt(portString);
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("获取服务端口出错 " + instance, e);
            }
        }


        private boolean needsToUpdateConnections(final List<ServiceInstance> newInstanceList) {
            if (this.savedInstanceList.size() != newInstanceList.size()) {
                return true;
            }
            for (final ServiceInstance instance : this.savedInstanceList) {
                final int port = getGRPCPort(instance);
                boolean isSame = false;
                for (final ServiceInstance newInstance : newInstanceList) {
                    final int newPort = getGRPCPort(newInstance);
                    if (newInstance.getHost().equals(instance.getHost())
                            && port == newPort) {
                        isSame = true;
                        break;
                    }
                }
                if (!isSame) {
                    return true;
                }
            }
            return false;
        }

    }

}
