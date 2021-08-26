package org.agamotto.cloud.controller;


import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.exception.AgamottoBusinessException;
import org.agamotto.cloud.exception.SampleError;
import org.agamotto.cloud.resp.AgamottoResponse;
import org.agamotto.cloud.resp.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${property.from.sample.custom.source}")
    private String source;

    @PostConstruct
    public void init() {
        log.info("{}", source);
    }

    @GetMapping("/services")
    public Ret<?> getServices() {
        return AgamottoResponse.ok(discoveryClient.getServices());
    }


    @GetMapping("/service")
    public Ret<?> getServicesxxx(@RequestParam("service") String service) {
        return AgamottoResponse.ok(discoveryClient.getInstances(service));
    }

    @GetMapping("/exception")
    public Ret<?> getException() {
        SampleError.ERROR_PARAM.toThrow();
        return AgamottoResponse.ok();
    }
}

