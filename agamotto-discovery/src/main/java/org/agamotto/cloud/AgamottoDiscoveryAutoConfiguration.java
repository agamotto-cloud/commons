package org.agamotto.cloud;


import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.discovery.AgamottoDiscoveryClient;
import org.agamotto.cloud.serviceregistry.AgamottoServiceInstance;
import org.agamotto.cloud.serviceregistry.AgamottoServiceRegistration;
import org.agamotto.cloud.serviceregistry.AgamottoServiceRegistry;
import org.agamotto.cloud.util.TtlScheduler;
import org.agamotto.cloud.util.ServiceUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class AgamottoDiscoveryAutoConfiguration {



    @Bean
    public AgamottoServiceInstance agamottoServiceInstance(Environment environment, ServerProperties serverProperties, InetUtils inetUtils) {
        //实例化当前服务实例
        AgamottoServiceInstance agamottoServiceInstance = new AgamottoServiceInstance();
        agamottoServiceInstance.setServiceId(ServiceUtils.getServiceName(environment));
        agamottoServiceInstance.setHost(inetUtils.findFirstNonLoopbackHostInfo().getIpAddress());
        agamottoServiceInstance.setPort(serverProperties.getPort());
        agamottoServiceInstance.setInstanceId(agamottoServiceInstance.getServiceId() + ":" + agamottoServiceInstance.getHost() + ":" + agamottoServiceInstance.getPort());
        return agamottoServiceInstance;
    }

    @Bean
    public AgamottoServiceRegistry agamottoServiceRegistry(RedissonClient redissonClient) {
        //服务注册使用的组件
        return new AgamottoServiceRegistry(redissonClient);
    }


    @Bean
    public AgamottoServiceRegistration agamottoServiceRegistration(AutoServiceRegistrationProperties properties,
                                                                   AgamottoServiceRegistry agamottoServiceRegistry,
                                                                   AgamottoServiceInstance agamottoServiceInstance) {
        //实例化服务注册中的实例管理
        return new AgamottoServiceRegistration(properties, agamottoServiceRegistry, agamottoServiceInstance);
    }


    @Bean
    public AgamottoDiscoveryClient agamottoDiscoveryClient(RedissonClient redissonClient) {
        //实例服务注册与发现组件
        return new AgamottoDiscoveryClient(redissonClient);
    }

}
