/*
 * Copyright 2013-2019 the original author or authors.
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

package org.agamotto.cloud.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.config.exception.ConfigException;
import org.agamotto.cloud.nacos.NacosProperties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 */
@Slf4j
//@Configuration
@Deprecated
public class NacosConfigServicePropertySourceLocator implements PropertySourceLocator {

    private final ConfigService configService;
    private final NacosProperties properties;

    public NacosConfigServicePropertySourceLocator(NacosProperties properties) throws NacosException {
        configService = NacosFactory.createConfigService(properties.getServerAddr());
        this.properties = properties;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        log.info("获取nacos配置{}", properties);
        try {
            String content = configService.getConfig(properties.getServiceName(), properties.getProfile(), 3000);
            if (StringUtils.isEmpty(content)) {
                throw new ConfigException("未在nacos上拉取到配置");
            }
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8)));
            return new PropertiesPropertySource("nacos-config", Objects.requireNonNull(yaml.getObject()));
        } catch (NacosException e) {
            throw new ConfigException(e.getMessage());
        }
    }
}
