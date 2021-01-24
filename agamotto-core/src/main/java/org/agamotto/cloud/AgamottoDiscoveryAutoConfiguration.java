package org.agamotto.cloud;


import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.client.AgamottoDiscoveryClient;
import org.agamotto.cloud.nacos.NacosProperties;
import org.agamotto.cloud.nacos.NacosServiceManager;
import org.agamotto.cloud.serviceregistry.AgamottoServiceInstance;
import org.agamotto.cloud.serviceregistry.AgamottoServiceRegistration;
import org.agamotto.cloud.serviceregistry.AgamottoServiceRegistry;
import org.agamotto.cloud.util.ServiceUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Slf4j
public class AgamottoDiscoveryAutoConfiguration {

    public AgamottoDiscoveryAutoConfiguration() {
        log.info("应用服务发现与注册");
    }



    @Bean
    public AgamottoServiceRegistration agamottoServiceRegistration(AutoServiceRegistrationProperties properties,
                                                                   AgamottoServiceRegistry agamottoServiceRegistry,
                                                                   AgamottoServiceInstance agamottoServiceInstance) {
        return new AgamottoServiceRegistration(properties, agamottoServiceRegistry,agamottoServiceInstance);
    }

    @Bean
    public AgamottoServiceInstance agamottoServiceInstance(Environment environment, ServerProperties serverProperties, InetUtils inetUtils) {

        AgamottoServiceInstance agamottoServiceInstance = new AgamottoServiceInstance();
        agamottoServiceInstance.setServiceId(ServiceUtils.getServiceName(environment));
        agamottoServiceInstance.setHost(inetUtils.findFirstNonLoopbackHostInfo().getIpAddress());
        agamottoServiceInstance.setPort(serverProperties.getPort());
        agamottoServiceInstance.setInstanceId(agamottoServiceInstance.getServiceId()+":"+agamottoServiceInstance.getHost()+":"+agamottoServiceInstance.getPort());
        return agamottoServiceInstance;

    }


    @Bean
    public NacosServiceManager nacosServiceManager(NacosProperties nacosDiscoveryProperties) {

        return new NacosServiceManager(nacosDiscoveryProperties);
    }
    @Bean
    public AgamottoServiceRegistry agamottoServiceRegistry(NacosServiceManager nacosServiceManager) {

        return new AgamottoServiceRegistry(nacosServiceManager.getNacosService());
    }

    @Bean
    public AgamottoDiscoveryClient agamottoDiscoveryClient(NacosServiceManager nacosServiceManager) {
        return new AgamottoDiscoveryClient(nacosServiceManager.getNacosService());
    }

}
