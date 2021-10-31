package org.agamotto.cloud.util;

import org.agamotto.cloud.Constant;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class ServiceUtils {

    public static String getServiceName(Environment environment) {
        String serviceName = environment.getProperty(Constant.AGAMOTTO_PROPERTIES_PREFIX + "." + Constant.SERVICE_NAME_KEY);
        if (StringUtils.hasText(serviceName)) {
            return serviceName;
        }

        serviceName = environment.getProperty("spring.application.name");
        if (StringUtils.hasText(serviceName)) {
            return serviceName;
        }
        return getServiceName();
    }

    public static String getServiceEnv(Environment environment) {
        String env = environment.getProperty(Constant.AGAMOTTO_PROPERTIES_PREFIX + "." + Constant.SERVICE_ENV_KEY);
        if (StringUtils.hasText(env)) {
            return env;
        }
        env = environment.getProperty("spring.profiles.active");
        if (StringUtils.hasText(env)) {
            return env;
        }
        return "pro";
    }



    private static String getServiceName() {

        Class<?> mainClass = ConfigUtils.deduceMainApplicationClass();
        if (mainClass == null) {
            return null;
        }
        String serviceName = mainClass.getSimpleName();
        serviceName = serviceName.replace("Application", "");
        char[] serviceNameChars = serviceName.toCharArray();
        serviceNameChars[0] = Character.toLowerCase(serviceNameChars[0]);
        StringBuilder targetServiceName = new StringBuilder();
        for (char serviceNameChar : serviceNameChars) {
            if (serviceNameChar >= 'a') {
                targetServiceName.append(serviceNameChar);
            } else {
                targetServiceName.append("-").append((char) (serviceNameChar + 32));
            }
        }
        return targetServiceName.toString();

    }
}

