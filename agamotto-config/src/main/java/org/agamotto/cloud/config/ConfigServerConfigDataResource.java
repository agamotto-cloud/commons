package org.agamotto.cloud.config;

import lombok.Data;
import org.springframework.boot.context.config.ConfigDataResource;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigServerConfigDataResource extends ConfigDataResource {
    /**
     * 服务指定的配置
     */
    private List<String> configYaml = new ArrayList<>();

    /**
     * 全局配置
     */
    private List<String> globalConfigYaml = new ArrayList<>();

}
