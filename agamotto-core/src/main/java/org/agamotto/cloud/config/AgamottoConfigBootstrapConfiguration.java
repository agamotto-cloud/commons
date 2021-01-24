/*
 * Copyright 2013-2018 the original author or authors.
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
import org.agamotto.cloud.nacos.NacosProperties;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;


/**
 * @author dongwenqiang
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@Order
public class AgamottoConfigBootstrapConfiguration {



    @Bean
    public NacosProperties configClientProperties(Environment environment,
                                                           ApplicationContext context) {
        if (context.getParent() != null
                && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                context.getParent(), NacosProperties.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(),
                    NacosProperties.class);
        }
        return new NacosProperties(environment,context.getId());
    }




}
