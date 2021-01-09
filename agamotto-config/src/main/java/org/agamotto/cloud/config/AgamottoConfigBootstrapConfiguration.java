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

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * @author dongwenqiang
 */
@Slf4j
@Configuration(proxyBeanMethods = false)

@ConditionalOnProperty(name = AgamottoConfigProperties.PREFIX + ".enabled", matchIfMissing = true)
public class AgamottoConfigBootstrapConfiguration {

    @Bean
    public AgamottoConfigProperties configClientProperties(Environment environment,
                                                           ApplicationContext context) {

        if (context.getParent() != null
                && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                context.getParent(), AgamottoConfigProperties.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(),
                    AgamottoConfigProperties.class);
        }
        return new AgamottoConfigProperties(environment,context.getId());
    }

    @Bean
    public PropertySourceLocator nacosConfigServicePropertySource(
            AgamottoConfigProperties properties) throws NacosException {
        return new NacosConfigServicePropertySourceLocator(
                properties);
    }


}
