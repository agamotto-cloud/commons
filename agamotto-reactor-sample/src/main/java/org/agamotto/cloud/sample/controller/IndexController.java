package org.agamotto.cloud.sample.controller;


import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.discovery.reactive.AgamottoReactiveDiscoveryClient;
import org.agamotto.cloud.resp.AgamottoResponse;
import org.agamotto.cloud.resp.Ret;
import org.agamotto.cloud.sample.exception.SampleError;
import org.agamotto.cloud.sample.rpc.SampleRpcClient;
import org.agamotto.cloud.sample.status.SampleStatus;
import org.agamotto.cloud.status.StatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private AgamottoReactiveDiscoveryClient discoveryClient;

    @Value("${property.from.sample.custom.source}")
    private String source;

    @PostConstruct
    public void init() {
        log.info("{}", source);
    }

    @GetMapping("/services")
    public Mono<Ret<?>> getServices() {
        //ErrorWebFluxAutoConfiguration;
        return discoveryClient.getServices().collectList().map(AgamottoResponse::ok);
    }


    @GetMapping("/service")
    public Mono<Ret<?>> getServicesxxx(@RequestParam("service") String service) {
        return discoveryClient.getInstances(service).collectList().map(AgamottoResponse::ok);
    }

    @GetMapping("/exception")
    public Ret<?> getException() {
        SampleError.ERROR_PARAM.toThrow();
        return AgamottoResponse.ok();
    }

    @GetMapping("/status")
    public Mono<Ret<Map<?, ?>>> getStatus() {
        return AgamottoResponse.okReactive(StatusUtils.getAll(SampleStatus.class));
    }

    @Autowired
    private SampleRpcClient sampleRpcClient;

    @GetMapping("/rpc")
    public Mono<Ret<String>> handleRpc(@RequestParam("msg") String msg) {
        return AgamottoResponse.okReactive(sampleRpcClient.callService(msg));

    }
}

