package org.agamotto.cloud.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.util.ServiceUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Data
@ConfigurationProperties(NacosProperties.PREFIX)
@EnableConfigurationProperties(NacosProperties.class)
public class NacosProperties {
    public static final String PREFIX = "spring.cloud.agamotto.nacos";

    private List<String> server = Collections.singletonList("192.168.44.56:8848");
    private String profile;
    private Boolean enabled = true;
    private String serviceName = "";

    public NacosProperties(){
        log.info("nacos-init");
    }

    public NacosProperties(Environment environment, String id) {

        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }
        this.setProfile(StringUtils.arrayToCommaDelimitedString(profiles));

        serviceName = ServiceUtils.getServiceName(environment);
    }

    public String getServerAddr(){
        StringBuilder serverAdd = new StringBuilder();
        for (String s : this.getServer()) {
            serverAdd.append(s).append(",");
        }
        return serverAdd.substring(0,serverAdd.length()-1);
    }
}
