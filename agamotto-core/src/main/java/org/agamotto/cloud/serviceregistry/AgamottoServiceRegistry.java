package org.agamotto.cloud.serviceregistry;


import com.alibaba.nacos.api.exception.NacosException;

import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * 这里应该是和第三方注册中心对接
 */
@Slf4j
public class AgamottoServiceRegistry implements ServiceRegistry<AgamottoServiceInstance> {

    private AgamottoServiceInstance agamottoServiceInstance;
    private NamingService namingService;
    public AgamottoServiceRegistry(NamingService namingService){
       this.namingService = namingService;

    }

    @Override
    public void register(AgamottoServiceInstance agamottoServiceInstance) {
        log.info("注册服务:{}.{},{}",agamottoServiceInstance.getServiceId(),agamottoServiceInstance.getInstanceId(),agamottoServiceInstance);
        this.agamottoServiceInstance = agamottoServiceInstance;
        try {
            namingService.registerInstance(agamottoServiceInstance.getServiceId(),agamottoServiceInstance.getHost(),agamottoServiceInstance.getPort());
        } catch (NacosException e) {
            log.error("nacos出错,{}",e.getErrMsg());
            throw new RuntimeException(e);
        }


    }

    @Override
    public void deregister(AgamottoServiceInstance registration) {
        log.info("注销服务{}",registration);
        try {
            namingService.deregisterInstance(agamottoServiceInstance.getServiceId(),agamottoServiceInstance.getHost(),agamottoServiceInstance.getPort());
        } catch (NacosException e) {
            log.error("nacos出错,{}",e.getErrMsg());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        log.info("关闭");
    }

    @Override
    public void setStatus(AgamottoServiceInstance registration, String status) {
        log.info("设置服务状态{}:{}",status,registration);
    }

    @Override
    public <T> T getStatus(AgamottoServiceInstance registration) {
        log.info("获取服务状态{}:{}",registration);

        return null;
    }
}
