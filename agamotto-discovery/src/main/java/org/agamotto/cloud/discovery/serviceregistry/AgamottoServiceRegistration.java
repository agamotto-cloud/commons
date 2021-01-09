package org.agamotto.cloud.discovery.serviceregistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;


/**
 * 管理服务注册生命周期
 */
@Slf4j
public class AgamottoServiceRegistration extends AbstractAutoServiceRegistration<AgamottoServiceInstance>   {

    private AgamottoServiceInstance agamottoServiceInstance;
    private  AgamottoServiceRegistry serviceRegistry;
    public AgamottoServiceRegistration(AutoServiceRegistrationProperties properties,AgamottoServiceRegistry serviceRegistry) {
        super(serviceRegistry, properties);
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    @Deprecated
    protected Object getConfiguration() {
        log.warn("获取服务配置");
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected AgamottoServiceInstance getRegistration() {
        AgamottoServiceInstance agamottoServiceInstance = new AgamottoServiceInstance();
        agamottoServiceInstance.setInstanceId("xxx");
        return agamottoServiceInstance;
    }

    @Override
    protected AgamottoServiceInstance getManagementRegistration() {
        AgamottoServiceInstance agamottoServiceInstance = new AgamottoServiceInstance();
        agamottoServiceInstance.setInstanceId("xxx");
        return agamottoServiceInstance;
    }



}
