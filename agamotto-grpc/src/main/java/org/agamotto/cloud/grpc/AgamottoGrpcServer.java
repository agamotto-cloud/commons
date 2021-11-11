package org.agamotto.cloud.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.exception.AgamottoDefaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class AgamottoGrpcServer implements SmartLifecycle {

    private volatile boolean running = false;
    private Server server;
    @Autowired
    private ServerProperties serverProperties;

    @Autowired(required = false)
    private List<BindableService> bindableServiceList;
    @Autowired
    private DiscoveryClient discoveryClient;
    private ThreadPoolExecutor executorService;

    @Override
    public void start() {
        if (CollectionUtils.isEmpty(bindableServiceList)) {
            log.warn("未发现有需要注册的grpc服务");
            return;
        }
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(serverProperties.getPort() + 1);
        for (BindableService bindableService : bindableServiceList) {
            serverBuilder.addService(bindableService);
            log.info("addService: {}", bindableService.getClass().getSimpleName());
        }
        serverBuilder = serverBuilder.executor(Executors.newSingleThreadScheduledExecutor());
        try {
            server = serverBuilder.build().start();
            log.info("grpc服务已启动 port {}", server.getPort());
        } catch (IOException e) {
            log.info("气动失败", e);
            throw AgamottoDefaultException.build(e);
        }
        running = true;
    }

    @Override
    public void stop() {
        running = false;
        if (server != null) {
            server.shutdown();
            server = null;
        }
        log.info("grpc服务已关闭 port ");
    }

    @Override
    public boolean isRunning() {
        return running;
    }


}
