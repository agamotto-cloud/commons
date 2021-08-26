package org.agamotto.cloud.serviceregistry;

import lombok.Data;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

/**
 * 描述服务实例的实体类
 */
@Data
public class AgamottoServiceInstance implements Registration, Serializable {

    private String serviceId;

    private String host;

    private int port;

    private boolean secure;

    private URI uri;

    private Map<String, String> metadata;

    private String instanceId;

    private String scheme;

    private Long registryTimestamp;
}
