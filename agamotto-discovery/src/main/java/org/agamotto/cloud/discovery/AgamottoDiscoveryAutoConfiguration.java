package org.agamotto.cloud.discovery;


import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.discovery.client.AgamottoDiscoveryClient;
import org.agamotto.cloud.discovery.serviceregistry.AgamottoServiceRegistration;
import org.agamotto.cloud.discovery.serviceregistry.AgamottoServiceRegistry;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
//@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled", matchIfMissing = true)
public class AgamottoDiscoveryAutoConfiguration {

    public AgamottoDiscoveryAutoConfiguration(){
        log.info("应用服务发现与注册");
    }


    @Bean
    public AgamottoServiceRegistry agamottoServiceRegistry() {
        return new AgamottoServiceRegistry();
    }

    @Bean
    public AgamottoServiceRegistration agamottoServiceRegistration(AutoServiceRegistrationProperties properties) {
        return new AgamottoServiceRegistration(properties,agamottoServiceRegistry());
    }

    @Bean
    public AgamottoDiscoveryClient agamottoDiscoveryClient(){
        return  new AgamottoDiscoveryClient();
    }

}
