package org.agamotto.cloud.grpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;

@Slf4j
public class AgamottoGrpcAutoConfiguration {

    @Bean
    public AgamottoGrpcServer agamottoGrpcServer() {
        return new AgamottoGrpcServer();
    }


    @Bean
    public RpcClientBuilder rpcClientBuilder(DiscoveryClient discoveryClient) {
        return new RpcClientBuilder(discoveryClient);
    }

}
