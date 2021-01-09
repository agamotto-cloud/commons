package org.agamotto.cloud.discovery.serviceregistry;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * 这里应该是和第三方注册中心对接
 */
@Slf4j
public class AgamottoServiceRegistry implements ServiceRegistry<AgamottoServiceInstance> {

    @Override
    public void register(AgamottoServiceInstance registration) {
        log.info("注册服务{}",registration);
    }

    @Override
    public void deregister(AgamottoServiceInstance registration) {
        log.info("注销服务{}",registration);
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
