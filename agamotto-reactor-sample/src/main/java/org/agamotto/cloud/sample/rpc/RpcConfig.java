package org.agamotto.cloud.sample.rpc;

import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import org.agamotto.cloud.grpc.nameresolver.DiscoveryClientResolverFactory;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class RpcConfig {
    @Bean
    public SimpleSampleGrpc.SimpleSampleStub simpleSampleStub(DiscoveryClient discoveryClient) {
        NameResolverRegistry.getDefaultRegistry().register(new DiscoveryClientResolverFactory(discoveryClient));
        return SimpleSampleGrpc.newStub(ManagedChannelBuilder.forTarget("agamotto:/axcc").usePlaintext().executor(Executors.newSingleThreadScheduledExecutor()).build());
    }

}
