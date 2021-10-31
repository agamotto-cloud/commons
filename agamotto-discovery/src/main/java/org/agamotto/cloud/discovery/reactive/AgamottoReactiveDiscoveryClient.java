/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.agamotto.cloud.discovery.reactive;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.Constant;
import org.agamotto.cloud.discovery.AgamottoDiscoveryClient;
import org.agamotto.cloud.serviceregistry.AgamottoServiceInstance;
import org.agamotto.cloud.util.TtlScheduler;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  {@link ReactiveDiscoveryClient}.
 *
 * @author Tim Ysewyn
 * @author Chris Bono
 */
@Slf4j
public class AgamottoReactiveDiscoveryClient implements ReactiveDiscoveryClient {


    private final RedissonReactiveClient redissonClient;
    private final Map<String, AgamottoDiscoveryClient.ServiceInstanceMapData> serviceInstanceMap = new ConcurrentHashMap<>();

    private List<String> services = null;

    @Autowired
    private AgamottoServiceInstance agamottoServiceInstance;

    @Autowired
    private TtlScheduler scheduler;

    public AgamottoReactiveDiscoveryClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient.reactive();
    }

    @Override
    public String description() {
        return "Spring Cloud agamotto Reactive Discovery Client";
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceId) {

        return Flux.defer(() -> {
            if (serviceInstanceMap.containsKey(serviceId)) {
                AgamottoDiscoveryClient.ServiceInstanceMapData instances = serviceInstanceMap.get(serviceId);
                if (instances.time > (System.currentTimeMillis() + Duration.ofMinutes(30).toMillis())) {
                    return Flux.fromIterable(instances.serviceInstanceList);
                }
            }
            scheduler.add(serviceId + "getInstances", () -> getRemoteInstances(serviceId));
            return getRemoteInstances(serviceId);
        }).onErrorResume(exception -> {
            log.error("获取服务实例失败", exception);
            return Flux.empty();
        }).subscribeOn(Schedulers.boundedElastic());
    }


    @Override
    public Flux<String> getServices() {
        return Flux.defer(() -> {
            if (services != null) {
                return Flux.fromIterable(services);
            }
            scheduler.add("getServices", this::getRemoteServices);
            return getRemoteServices();
        }).onErrorResume(exception -> {
            log.error("获取服务列表失败", exception);
            return Flux.empty();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<String> getRemoteServices() {
        try {
            RMapReactive<String, String> instanceMap = redissonClient.getMap(Constant.DISCOVER_PREFIX_KEY +agamottoServiceInstance.getEnv() + ":"+ "list");
            return instanceMap.isExists().flatMapMany(isExists -> {
                if (isExists) {
                    return instanceMap.valueIterator().collectList().map(v -> {
                        this.services = v;
                        return v;
                    }).flatMapIterable(v -> v);
                } else {
                    log.error("获取服务列表失败,未找到实例");
                    return Flux.empty();
                }
            });
        } catch (Exception e) {
            log.error("获取服务列表失败", e);
            throw new RuntimeException(e);
        }
    }

    private Flux<ServiceInstance> getRemoteInstances(String serviceId) {
        try {
            RMapReactive<String, AgamottoServiceInstance> instanceMap = redissonClient.getMap(Constant.DISCOVER_PREFIX_KEY + serviceId);
            return instanceMap.isExists().flatMapMany(isExists -> {
                if (isExists) {
                    return instanceMap.valueIterator().collectList().map(agamottoInstanceList -> {
                        ArrayList<ServiceInstance> instanceList = new ArrayList<>();
                        for (AgamottoServiceInstance value : agamottoInstanceList) {
                            if (value.getRegistryTimestamp() == null) {
                                continue;
                            }
                            if ((value.getRegistryTimestamp() + Duration.ofSeconds(30).toMillis()) > System.currentTimeMillis()) {
                                instanceList.add(value);
                            } else {
                                instanceMap.remove(value.getInstanceId());
                            }
                        }
                        serviceInstanceMap.put(serviceId, new AgamottoDiscoveryClient.ServiceInstanceMapData(System.currentTimeMillis(), instanceList));
                        return instanceList;
                    }).flatMapIterable(v -> v);
                } else {
                    log.error("获取服务列表失败,未找到实例");
                    return Flux.empty();
                }
            });

        } catch (Exception e) {
            log.error("获取服务{}实例列表出错,{}", serviceId, e);
            throw new RuntimeException(e);
        }
    }
}
