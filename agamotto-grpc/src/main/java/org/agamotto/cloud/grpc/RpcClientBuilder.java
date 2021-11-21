package org.agamotto.cloud.grpc;


import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import org.agamotto.cloud.grpc.nameresolver.DiscoveryClientResolverFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.concurrent.Executors;

public class RpcClientBuilder {

    public RpcClientBuilder(DiscoveryClient discoveryClient) {
        NameResolverRegistry.getDefaultRegistry().register(new DiscoveryClientResolverFactory(discoveryClient));
    }


    public  ManagedChannelBuilder<?> forServerName(String name) {
        return ManagedChannelBuilder.forTarget("agamotto:/" + name).usePlaintext().executor(Executors.newSingleThreadScheduledExecutor());
    }
}
