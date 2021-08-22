package org.agamotto.cloud.serviceregistry;


import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.config.Constant;
import org.agamotto.cloud.util.TtlScheduler;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * 这里应该是和第三方注册中心对接
 */
@Slf4j
public class AgamottoServiceRegistry implements ServiceRegistry<AgamottoServiceInstance> {

    private final RedissonClient redissonClient;


    @Autowired
    private TtlScheduler scheduler;

    public AgamottoServiceRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    @Override
    public void register(AgamottoServiceInstance agamottoServiceInstance) {
        log.info("注册服务:{}.{},{}", agamottoServiceInstance.getServiceId(), agamottoServiceInstance.getInstanceId(), agamottoServiceInstance);
        try {
            updateServiceInfo(agamottoServiceInstance);
            scheduler.add(agamottoServiceInstance.getInstanceId()+"refister-refresh", () -> {
                updateServiceInfo(agamottoServiceInstance);
            });
        } catch (Exception e) {
            log.error("注册出错,{}", e);
            throw new RuntimeException(e);
        }
    }

    private void updateServiceInfo(AgamottoServiceInstance agamottoServiceInstance) {
        String serviceId = agamottoServiceInstance.getServiceId();
        try {
            RMap<String, String> serviceMap = redissonClient.getMap(Constant.DISCOVER_KEY + ":list");
            if (!serviceMap.containsKey(serviceId)) {
                serviceMap.put(serviceId, serviceId);
            }
            RMap<String, AgamottoServiceInstance> instanceMap = redissonClient.getMap(Constant.DISCOVER_KEY + ":" + serviceId);
            agamottoServiceInstance.setRegistryTimestamp(System.currentTimeMillis());
            instanceMap.put(agamottoServiceInstance.getInstanceId(), agamottoServiceInstance);

        } catch (Exception e) {
            log.error("注册出错", e);
            throw new RuntimeException(e);
        }
    }


    private void configGateway(String serviceId, String keyPre) {
        RBucket<String> ruleKey = redissonClient.getBucket(keyPre + "/rule");
        if (!ruleKey.isExists()) {
            ruleKey.set(serviceId + "@Docker");
        }
        RBucket<String> serviceKey = redissonClient.getBucket(keyPre + "/service");
        if (!serviceKey.isExists()) {
            serviceKey.set("PathPrefix(`/" + serviceId + "`)");
        }
        RBucket<String> key = redissonClient.getBucket(keyPre + "/middlewares/0");
        if (!key.isExists()) {
            key.set("remove-prefix");
        }
    }

    @Override
    public void deregister(AgamottoServiceInstance registration) {
        log.info("注销服务{}", registration);
        String serviceId = registration.getServiceId();
        try {
            scheduler.remove(registration.getInstanceId()+"refister-refresh");
            RMap<String, Long> instanceMap = redissonClient.getMap(Constant.DISCOVER_KEY + ":" + serviceId);
            instanceMap.remove(registration.getInstanceId());
        } catch (Exception e) {
            log.error("注销服务出错", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        log.info("关闭");
    }

    @Override
    public void setStatus(AgamottoServiceInstance registration, String status) {
        log.info("设置服务状态{}:{}", status, registration);
    }

    @Override
    public <T> T getStatus(AgamottoServiceInstance registration) {
        log.info("获取服务状态{}:{}", registration);
        return null;
    }

}
