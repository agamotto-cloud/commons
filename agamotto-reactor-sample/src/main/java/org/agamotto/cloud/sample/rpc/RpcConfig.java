package org.agamotto.cloud.sample.rpc;

import org.agamotto.cloud.grpc.RpcClientBuilder;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class RpcConfig {


    @Bean
    public SimpleSampleGrpc.SimpleSampleStub simpleSampleStub(RpcClientBuilder rpcClientBuilder) {
        return SimpleSampleGrpc.newStub(rpcClientBuilder.forServerName("axcc").build());
    }

    @Bean
    public SimpleSampleGrpc.SimpleSampleBlockingStub simpleSampleBlockingStub(RpcClientBuilder rpcClientBuilder) {
        return SimpleSampleGrpc.newBlockingStub(rpcClientBuilder.forServerName("axcc").build());
    }



    @Bean
    public Scheduler publishScheduler() {
        return Schedulers.newParallel("grpc-request-handle");
    }

    @Bean
    public Scheduler subscribeScheduler() {
        return Schedulers.newParallel("grpc-result-handle");
    }
}
