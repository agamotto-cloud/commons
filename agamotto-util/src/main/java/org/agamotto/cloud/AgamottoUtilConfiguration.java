package org.agamotto.cloud;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.util.TtlScheduler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


@Slf4j
@Configuration(proxyBeanMethods = false)
public class AgamottoUtilConfiguration {


    @Bean
    @ConditionalOnMissingBean(Config.class)
    public Config redissonClientConfig() {
        return loadProperties();
    }

    @Bean
    public TtlScheduler ttlScheduler() {
        return new TtlScheduler();
    }

    @Bean
    public RedissonClient redissonClient(Config redissonClientConfig) {
        return createRedissonClient(redissonClientConfig);
    }

    public static RedissonClient createRedissonClient(Config redissonClientConfig) {
        try {
            return Redisson.create(redissonClientConfig);
        } catch (Exception e) {
            log.error("redis连接失败", e);
            throw e;
        }
    }

    private static final String ppss = "dong";
    private static final String ppp = 1 + "995" + 1 + "018";

    public static Config loadProperties() {
        Config config = new Config();
        SingleServerConfig builder = config.useSingleServer().setAddress("redis://api.agamotto.cloud:6379");
        builder.setPassword(ppss + ppp);
        config.setCodec(JsonJacksonCodec.INSTANCE);
        return config;
    }

}
