package org.agamotto.cloud.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.config.exception.ConfigException;
import org.agamotto.cloud.nacos.NacosProperties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

@Slf4j
public class AgamottoConfigServerConfigDataLoader implements ConfigDataLoader<ConfigServerConfigDataResource> {


    private ConfigService configService;

    public AgamottoConfigServerConfigDataLoader(){
        log.info("初始化");
    }

    @Override
    public ConfigData load(ConfigDataLoaderContext context, ConfigServerConfigDataResource resource) throws ConfigDataResourceNotFoundException {
        ConfigData configData = null;
        NacosProperties properties = resource.getProperties();

        try {
            configService = NacosFactory.createConfigService(properties.getServerAddr());
            log.info("获取nacos配置{}", properties);

            String content = configService.getConfig(properties.getServiceName(), resource.getProfiles(), 3000);
            if (StringUtils.isEmpty(content)) {
                throw new ConfigException(properties.getServiceName()+"("+resource.getProfiles()+")未在nacos上拉取到配置");
            }
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8)));
            configData = new ConfigData(Collections.singleton(new PropertiesPropertySource("nacos-config", Objects.requireNonNull(yaml.getObject()))));
        } catch (NacosException e) {
            log.error("", e);
        }
        return configData;
    }





}
