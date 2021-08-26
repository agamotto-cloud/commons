package org.agamotto.cloud.config;

import lombok.Data;
import org.agamotto.cloud.redis.NacosProperties;
import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.boot.context.config.Profiles;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Data
public class ConfigServerConfigDataResource extends ConfigDataResource {
    private final NacosProperties properties;

    private final boolean optional;

    private final Profiles profiles;

    public ConfigServerConfigDataResource(NacosProperties properties, boolean optional, Profiles profiles) {
        this.properties = properties;
        this.optional = optional;
        this.profiles = profiles;
    }

    public NacosProperties getProperties() {
        return this.properties;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public String getProfiles() {
        List<String> accepted = profiles.getAccepted();
        if (StringUtils.hasText(properties.getProfile())) {
            return properties.getProfile();
        }
        return StringUtils.collectionToCommaDelimitedString(accepted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigServerConfigDataResource that = (ConfigServerConfigDataResource) o;
        return Objects.equals(this.properties, that.properties) && Objects.equals(this.optional, that.optional)
                && Objects.equals(this.profiles, that.profiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.properties, this.optional, this.profiles);
    }


}
