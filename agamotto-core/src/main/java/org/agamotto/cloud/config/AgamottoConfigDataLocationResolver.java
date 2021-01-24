package org.agamotto.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.nacos.NacosProperties;
import org.springframework.boot.context.config.*;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.agamotto.cloud.util.ServiceUtils.getServiceName;

@Slf4j
public class AgamottoConfigDataLocationResolver implements ConfigDataLocationResolver<ConfigServerConfigDataResource> {

    private static final String PREFIX = "nacos:";



    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        return location.hasPrefix(PREFIX);
    }

    @Override
    public List<ConfigServerConfigDataResource> resolve(ConfigDataLocationResolverContext context,
                                                      ConfigDataLocation location) {
        try {
            return resolve(context, location.getNonPrefixedValue(PREFIX));
        }
        catch (IOException ex) {
            throw new ConfigDataLocationNotFoundException(location, ex);
        }
    }

    private List<ConfigServerConfigDataResource> resolve(ConfigDataLocationResolverContext context, String location)
            throws IOException {

        return Collections.emptyList();
    }

    @Override
    public List<ConfigServerConfigDataResource> resolveProfileSpecific(ConfigDataLocationResolverContext context, ConfigDataLocation location, Profiles profiles) throws ConfigDataLocationNotFoundException {
        Binder binder = context.getBinder();
        BindHandler bindHandler = getBindHandler(context);
        NacosProperties nacosProperties = binder
                .bind(NacosProperties.PREFIX, Bindable.of(NacosProperties.class), bindHandler)
                .orElseGet(NacosProperties::new);
        if(!StringUtils.hasText(nacosProperties.getServiceName())) {
            nacosProperties.setServiceName(getServiceName());
        }
        ConfigServerConfigDataResource configDataResource = new ConfigServerConfigDataResource(nacosProperties,true,profiles);
        List<ConfigServerConfigDataResource> locations = new ArrayList<>();
        locations.add(configDataResource);
        return locations;
    }

    private BindHandler getBindHandler(ConfigDataLocationResolverContext context) {
        return context.getBootstrapContext().getOrElse(BindHandler.class, null);
    }
}
