package org.agamotto.cloud.config;

import lombok.Data;
import org.springframework.boot.context.config.ConfigDataResource;

@Data
public class ConfigServerConfigDataResource extends ConfigDataResource {

    private String configString;


}
