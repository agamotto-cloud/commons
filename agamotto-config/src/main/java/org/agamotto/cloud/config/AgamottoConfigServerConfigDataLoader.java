package org.agamotto.cloud.config;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;


public class AgamottoConfigServerConfigDataLoader implements ConfigDataLoader<ConfigServerConfigDataResource> {

    private final Log log;

    public AgamottoConfigServerConfigDataLoader(Log log) {
        this.log = log;
    }

    @Override
    public ConfigData load(ConfigDataLoaderContext context, ConfigServerConfigDataResource resource) throws ConfigDataResourceNotFoundException {

        log.trace("解析拉取来的配置");
        ConfigData configData = null;
        Properties merged = new Properties();

        if (!CollectionUtils.isEmpty(resource.getGlobalConfigYaml())) {
            for (String yamlStr : resource.getGlobalConfigYaml()) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setResources(new ByteArrayResource(yamlStr.getBytes(StandardCharsets.UTF_8)));
                if (yaml.getObject() != null) {
                    merged.putAll(yaml.getObject());
                }

            }

        }
        if (!CollectionUtils.isEmpty(resource.getConfigYaml())) {
            for (String yamlStr : resource.getConfigYaml()) {
                YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
                yaml.setResources(new ByteArrayResource(yamlStr.getBytes(StandardCharsets.UTF_8)));
                if (yaml.getObject() != null) {
                    merged.putAll(yaml.getObject());
                }
            }
        }


        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("agamotto", merged);
        configData = new ConfigData(Arrays.asList(propertiesPropertySource));
        return configData;
    }


}
