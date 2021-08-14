package org.agamotto.cloud.discovery;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.config.Constant;
import org.agamotto.cloud.serviceregistry.AgamottoServiceInstance;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于获取客户端列表的
 */
@Slf4j
public class AgamottoDiscoveryClient implements DiscoveryClient {

    private final RedissonClient redissonClient;

    public AgamottoDiscoveryClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String description() {
        // log.info("描述");
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        try {
            try {
                RMap<String, AgamottoServiceInstance> instanceMap = redissonClient.getMap(Constant.DISCOVER_KEY + ":" + serviceId);
                if (instanceMap == null || !instanceMap.isExists()) {
                    return new ArrayList<>();
                }
                return new ArrayList<>(instanceMap.values());

            } catch (Exception e) {
                log.error("注销服务出错", e);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error("获取服务{}实例列表出错,{}", serviceId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getServices() {
        try {
            RMap<String, String> instanceMap = redissonClient.getMap(Constant.DISCOVER_KEY + ":list");
            if (instanceMap == null || !instanceMap.isExists()) {
                return new ArrayList<>();
            }
            return new ArrayList<>(instanceMap.values());
        } catch (Exception e) {
            log.error("获取服务列表失败", e);
            throw new RuntimeException(e);
        }
    }
}
