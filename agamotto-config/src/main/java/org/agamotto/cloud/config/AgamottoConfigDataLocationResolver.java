package org.agamotto.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.AgamottoUtilConfiguration;
import org.apache.commons.logging.Log;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.BootstrapContext;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.context.config.*;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AgamottoConfigDataLocationResolver implements ConfigDataLocationResolver<ConfigServerConfigDataResource> {

    public static final String PREFIX = "agamotto-cloud:";

    private final Log log;

    public AgamottoConfigDataLocationResolver(Log log) {
        this.log = log;
    }

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        if (!location.hasPrefix(PREFIX)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<ConfigServerConfigDataResource> resolve(ConfigDataLocationResolverContext context,
                                                        ConfigDataLocation location) {
        try {
            return resolve(context, location.getNonPrefixedValue(PREFIX));
        } catch (IOException ex) {
            throw new ConfigDataLocationNotFoundException(location, ex);
        }
    }

    private List<ConfigServerConfigDataResource> resolve(ConfigDataLocationResolverContext context, String location)
            throws IOException {
        return new ArrayList<>();
    }

    @Override
    public List<ConfigServerConfigDataResource> resolveProfileSpecific(ConfigDataLocationResolverContext context, ConfigDataLocation location, Profiles profiles) throws ConfigDataLocationNotFoundException {

        //从服务器上拉取下来配置

        // create redission   client
        registerBean(context, Config.class, AgamottoUtilConfiguration.loadProperties());

        registerAndPromoteBean(context, RedissonClient.class, this::createRedissonClient);
        ConfigurableBootstrapContext bootstrapContext = context.getBootstrapContext();
        RedissonClient redissonClient = bootstrapContext.get(RedissonClient.class);

        MapPropertySource xx = new MapPropertySource("customProperty",
                Collections.<String, Object>singletonMap("property.from.sample.custom.source", "worked as intended"));
        List<ConfigServerConfigDataResource> locations = new ArrayList<>();
          locations.add(new ConfigServerConfigDataResource());
        //解析
        return locations;
    }




    private BindHandler getBindHandler(ConfigDataLocationResolverContext context) {
        return context.getBootstrapContext().getOrElse(BindHandler.class, null);
    }

    public <T> void registerBean(ConfigDataLocationResolverContext context, Class<T> type, T instance) {
        context.getBootstrapContext().registerIfAbsent(type, BootstrapRegistry.InstanceSupplier.of(instance));
    }
    protected <T> void registerAndPromoteBean(ConfigDataLocationResolverContext context, Class<T> type,
                                              BootstrapRegistry.InstanceSupplier<T> supplier) {
        registerBean(context, type, supplier);
        context.getBootstrapContext().addCloseListener(event -> {
            T instance = event.getBootstrapContext().get(type);
            String name = "configData" + type.getSimpleName();
            ConfigurableApplicationContext appCtxt = event.getApplicationContext();
            if (!appCtxt.containsBean(name)) {
                appCtxt.getBeanFactory().registerSingleton(name, instance);
            }
        });
    }

    protected <T> void registerBean(ConfigDataLocationResolverContext context, Class<T> type,
                                    BootstrapRegistry.InstanceSupplier<T> supplier) {
        ConfigurableBootstrapContext bootstrapContext = context.getBootstrapContext();
        bootstrapContext.registerIfAbsent(type, supplier);
    }

    protected RedissonClient createRedissonClient(BootstrapContext context) {
        Config properties = context.get(Config.class);
        return AgamottoUtilConfiguration.createRedissonClient(properties);
    }



}


