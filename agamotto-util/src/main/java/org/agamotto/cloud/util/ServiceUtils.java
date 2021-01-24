package org.agamotto.cloud.util;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import static org.agamotto.cloud.GlobalVariable.AGAMOTTO_PROPERTIES_PREFIX;
import static org.agamotto.cloud.GlobalVariable.SERVICE_NAME_KEY;

public class ServiceUtils {

    public static String getServiceName(Environment environment) {
        String serviceName = environment.getProperty(AGAMOTTO_PROPERTIES_PREFIX + "." + SERVICE_NAME_KEY);
        if (StringUtils.hasText(serviceName)) {
            return serviceName;
        }


       return getServiceName();

    }
    public static String getServiceName() {

        Class<?> mainClass = ConfigUtils.deduceMainApplicationClass();
        if (mainClass == null) {
            return null;
        }
        String  serviceName = mainClass.getSimpleName();
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

