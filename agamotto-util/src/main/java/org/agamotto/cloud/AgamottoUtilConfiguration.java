package org.agamotto.cloud;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.redisson.operation.RedissonBinary;
import org.agamotto.cloud.redisson.operation.RedissonCollection;
import org.agamotto.cloud.redisson.operation.RedissonObject;
import org.agamotto.cloud.util.TtlScheduler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;


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

    @Bean
    public RedissonCollection redissonCollection(RedissonClient redissonClient){
        return new RedissonCollection(redissonClient);
    }
    @Bean
    public RedissonBinary redissonBinary(RedissonClient redissonClient){
        return new RedissonBinary(redissonClient);
    }
    @Bean
    public RedissonObject redissonObject(RedissonClient redissonClient){
        return new RedissonObject(redissonClient);
    }

    public static RedissonClient createRedissonClient(Config redissonClientConfig) {
        try {
            return Redisson.create(redissonClientConfig);
        } catch (Exception e) {
            log.error("redis连接失败", e);
            throw e;
        }
    }

    private static final String ppss = "YWdhbW90dG8xMjM=";

    public static Config loadProperties() {
        Config config = new Config();
        SingleServerConfig builder = config.useSingleServer().setAddress("redis://api.agamotto.cloud:8082");
        builder.setPassword(new String(Base64.getDecoder().decode(ppss)));
        builder.setConnectionPoolSize(3);
        builder.setConnectionMinimumIdleSize(2);
        builder.setSubscriptionConnectionMinimumIdleSize(2);
        builder.setSubscriptionConnectionPoolSize(3);
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.setNettyThreads(3);
        config.setThreads(3);
        return config;
    }

}
