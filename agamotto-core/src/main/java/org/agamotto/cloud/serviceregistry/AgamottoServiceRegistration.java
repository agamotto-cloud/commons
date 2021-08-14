package org.agamotto.cloud.serviceregistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;


/**
 * 管理服务注册生命周期
 */
@Slf4j
public class AgamottoServiceRegistration extends AbstractAutoServiceRegistration<AgamottoServiceInstance>   {


    private final AgamottoServiceInstance agamottoServiceInstance;
    private  AgamottoServiceRegistry serviceRegistry;

    public AgamottoServiceRegistration(AutoServiceRegistrationProperties properties, AgamottoServiceRegistry serviceRegistry, AgamottoServiceInstance agamottoServiceInstance) {
        super(serviceRegistry, properties);
        this.serviceRegistry = serviceRegistry;
        this.agamottoServiceInstance = agamottoServiceInstance;
    }


    @Override
    @Deprecated
    protected Object getConfiguration() {
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected AgamottoServiceInstance getRegistration() {
        //返回服务实例
        return agamottoServiceInstance;
    }

    @Override
    protected AgamottoServiceInstance getManagementRegistration() {
        //返回管理用的服务实例
        return agamottoServiceInstance;
    }




}
