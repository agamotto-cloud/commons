/*
 * Copyright 2015-2021 the original author or authors.
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

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.Constant;
import org.agamotto.cloud.util.ServiceUtils;
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;




public class AgamottoConfigDataMissingEnvironmentPostProcessor implements EnvironmentPostProcessor,Ordered {

    public static final String CONFIG_IMPORT_PROPERTY = "spring.config.import";
    private static final Bindable<String[]> CONFIG_DATA_LOCATION_ARRAY = Bindable.of(String[].class);


    private final Log log;

    public AgamottoConfigDataMissingEnvironmentPostProcessor(Log log) {
        this.log = log;
    }



    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

        List<String> property = getConfigImports(environment);
        if (property.isEmpty()) {
            Binder binder = Binder.get(environment);
            property = Arrays
                    .asList(binder.bind(CONFIG_IMPORT_PROPERTY, CONFIG_DATA_LOCATION_ARRAY).orElse(new String[0]));
        }

        if (hasAgamottoConfigImport(property)) {
            return;
        }
        log.trace("增加读取远程配置");
        MapPropertySource cloudConfigImport = new MapPropertySource("add-import-config",
                Collections.<String, Object>singletonMap(CONFIG_IMPORT_PROPERTY, ConfigDataLocation.OPTIONAL_PREFIX +
                        Constant.CONFIG_PREFIX_KEY+ ServiceUtils.getServiceName(environment)));
        propertySources.addLast(cloudConfigImport);
    }


    private List<String> getConfigImports(ConfigurableEnvironment environment) {
        List<String> property = environment.getProperty(CONFIG_IMPORT_PROPERTY, List.class);
        if (property == null || property.isEmpty()) {
            Binder binder = Binder.get(environment);
            property = Arrays
                    .asList(binder.bind(CONFIG_IMPORT_PROPERTY, CONFIG_DATA_LOCATION_ARRAY).orElse(new String[0]));
        }
        return property;
    }

    private boolean hasAgamottoConfigImport(List<String> property) {
        for (String s : property) {
            if (StringUtils.hasText(s) && s.startsWith(ConfigDataLocation.OPTIONAL_PREFIX + Constant.CONFIG_PREFIX_KEY)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
