package org.agamotto.cloud.discovery;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.Constant;
import org.agamotto.cloud.serviceregistry.AgamottoServiceInstance;
import org.agamotto.cloud.util.TtlScheduler;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于获取客户端列表的
 */
@Slf4j
public class AgamottoDiscoveryClient implements DiscoveryClient {

    private final RedissonClient redissonClient;

    public AgamottoDiscoveryClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private final Map<String, ServiceInstanceMapData> serviceInstanceMap = new ConcurrentHashMap<>();

    private List<String> services = null;


    @Autowired
    private TtlScheduler scheduler;

    @Override
    public String description() {
        return "Spring Cloud agamotto Discovery Client";
    }


    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        if (serviceInstanceMap.containsKey(serviceId)) {
            ServiceInstanceMapData instances = serviceInstanceMap.get(serviceId);
            if (instances.time > (System.currentTimeMillis() + Duration.ofMinutes(30).toMillis())) {
                return instances.serviceInstanceList;
            }
        }
        scheduler.add(serviceId + "getInstances", () -> getRemoteInstances(serviceId));
        return getRemoteInstances(serviceId);
    }



    @Override
    public List<String> getServices() {
        if (services != null) {
            return services;
        }
        scheduler.add("getServices", this::getRemoteServices);
        return getRemoteServices();
    }

    public List<String> getRemoteServices() {
        try {
            RMap<String, String> instanceMap = redissonClient.getMap(Constant.DISCOVER_PREFIX_KEY + ":list");
            if (instanceMap == null || !instanceMap.isExists()) {
                return new ArrayList<>();
            }
            this.services = new ArrayList<>(instanceMap.values());
            return services;
        } catch (Exception e) {
            log.error("获取服务列表失败", e);
            throw new RuntimeException(e);
        }
    }
    private List<ServiceInstance> getRemoteInstances(String serviceId) {
        try {
            RMap<String, AgamottoServiceInstance> instanceMap = redissonClient.getMap(Constant.DISCOVER_PREFIX_KEY + ":" + serviceId);
            if (instanceMap == null || !instanceMap.isExists()) {
                return new ArrayList<>();
            }
            ArrayList<ServiceInstance> instanceList = new ArrayList<>();
            for (AgamottoServiceInstance value : instanceMap.values()) {
                if (value.getRegistryTimestamp() == null) {
                    continue;
                }
                if ((value.getRegistryTimestamp() + Duration.ofSeconds(30).toMillis()) > System.currentTimeMillis()) {
                    instanceList.add(value);
                } else {
                    instanceMap.remove(value.getInstanceId());
                }
            }
            serviceInstanceMap.put(serviceId, new ServiceInstanceMapData(System.currentTimeMillis(), instanceList));
            return instanceList;
        } catch (Exception e) {
            log.error("获取服务{}实例列表出错,{}", serviceId, e);
            throw new RuntimeException(e);
        }
    }

    @AllArgsConstructor
    public static class ServiceInstanceMapData {
        public Long time;
        public List<ServiceInstance> serviceInstanceList;
    }

}
