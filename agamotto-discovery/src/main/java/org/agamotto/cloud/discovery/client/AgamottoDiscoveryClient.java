package org.agamotto.cloud.discovery.client;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.discovery.nacos.NacosDiscoveryProperties;
import org.agamotto.cloud.discovery.nacos.NacosServiceManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于获取客户端列表的
 */
@Slf4j
public class AgamottoDiscoveryClient implements DiscoveryClient {

    private NacosDiscoveryProperties discoveryProperties;

    private NacosServiceManager nacosServiceManager;

    public AgamottoDiscoveryClient(){
        log.info("創建客户端");
    }

    @Override
    public String description() {
       // log.info("描述");
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        //log.info("获取实例列表");
        return null;
    }

    @Override
    public List<String> getServices() {
        //log.info("获取服务列表");
        return null;
    }
}
