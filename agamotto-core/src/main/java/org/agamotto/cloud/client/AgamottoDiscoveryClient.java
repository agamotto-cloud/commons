package org.agamotto.cloud.client;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.serviceregistry.AgamottoServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于获取客户端列表的
 */
@Slf4j
public class AgamottoDiscoveryClient implements DiscoveryClient {



    private final NamingService nacosService;

    public AgamottoDiscoveryClient(NamingService nacosService){
        this.nacosService = nacosService;
    }

    @Override
    public String description() {
       // log.info("描述");
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        //log.info("获取实例列表");
        try {
            List<Instance> nacosServiceList = nacosService.getAllInstances(serviceId);
            return nacosServiceList.stream().map(nacosInstance->{
               AgamottoServiceInstance agamottoServiceInstance = new AgamottoServiceInstance();
               agamottoServiceInstance.setServiceId(nacosInstance.getServiceName());
               agamottoServiceInstance.setHost(nacosInstance.getIp());
               agamottoServiceInstance.setPort(nacosInstance.getPort());
               return agamottoServiceInstance;
            }).collect(Collectors.toList());
        } catch (NacosException e) {
            log.error("nacos出错,{}",e.getErrMsg());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getServices() {
        //log.info("获取服务列表");
        try {
            List<ServiceInfo> nacosServiceList = nacosService.getSubscribeServices();
            return nacosServiceList.stream().map(ServiceInfo::getName).collect(Collectors.toList());
        } catch (NacosException e) {
            log.error("nacos出错,{}",e.getErrMsg());
            throw new RuntimeException(e);
        }
    }
}
