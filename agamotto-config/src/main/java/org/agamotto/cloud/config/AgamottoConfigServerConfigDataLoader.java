package org.agamotto.cloud.config;

import org.apache.commons.logging.Log;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;


public class AgamottoConfigServerConfigDataLoader implements ConfigDataLoader<ConfigServerConfigDataResource> {

    private final Log log;

    public AgamottoConfigServerConfigDataLoader(Log log) {
        this.log = log;
    }

    @Override
    public ConfigData load(ConfigDataLoaderContext context, ConfigServerConfigDataResource resource) throws ConfigDataResourceNotFoundException {

        log.info("解析拉取来的配置");
        ConfigData configData = null;
        MapPropertySource xx = new MapPropertySource("customProperty",
                Collections.<String, Object>singletonMap("property.from.sample.custom.source", "worked as intended"));

        //YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        // yaml.setResources(new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8)));
        configData = new ConfigData(Collections.singleton(xx));
        return configData;
    }


}
