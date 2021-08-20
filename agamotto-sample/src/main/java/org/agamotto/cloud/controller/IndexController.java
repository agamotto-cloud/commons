package org.agamotto.cloud.controller;


import org.agamotto.cloud.exception.SampleError;
import org.agamotto.cloud.resp.AgamottoResponse;
import org.agamotto.cloud.resp.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private DiscoveryClient discoveryClient;

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

