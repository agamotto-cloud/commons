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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;


/**
 *
 */
@Slf4j
@Data
@ConfigurationProperties(AgamottoConfigProperties.PREFIX)
public class AgamottoConfigProperties {

    /**
     * Prefix of {@link AgamottoConfigProperties}.
     */
    public static final String PREFIX = "spring.cloud.agamotto.config";

    private String profile;

    private Boolean enabled = true;
    private String nacosServer = "192.168.44.56:8848";
    private String serviceName = "";

    public AgamottoConfigProperties(Environment environment, String id) {

        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }
        this.setProfile(StringUtils.arrayToCommaDelimitedString(profiles));


        if (!StringUtils.isEmpty(serviceName)) {

        } else if (!id.equals("application")) {
            this.serviceName = id;
        } else {
            Class<?> mainClass = ConfigUtils.deduceMainApplicationClass();
            String serviceName = mainClass.getSimpleName();
            serviceName = serviceName.replace("Application", "");
            char[] serviceNameChars = serviceName.toCharArray();
            serviceNameChars[0] = Character.toLowerCase(serviceNameChars[0]);
            StringBuilder targetServiceName = new StringBuilder();
            for (char serviceNameChar : serviceNameChars) {
                if (serviceNameChar >= 'a') {
                    targetServiceName.append(serviceNameChar);
                } else {
                    targetServiceName.append("-").append((char)(serviceNameChar + 32));
                }
            }
            this.serviceName = targetServiceName.toString();

        }
    }


}
