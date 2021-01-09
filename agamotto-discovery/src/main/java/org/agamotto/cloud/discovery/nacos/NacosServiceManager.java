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

package org.agamotto.cloud.discovery.nacos;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.commons.util.InetUtils;

import java.util.Objects;
import java.util.Properties;

import static com.alibaba.nacos.api.NacosFactory.createMaintainService;
import static com.alibaba.nacos.api.NacosFactory.createNamingService;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @author yuhuangbin
 */
@Slf4j
public class NacosServiceManager {


	private NacosDiscoveryProperties nacosDiscoveryPropertiesCache;

	private NamingService namingService;

	private NamingMaintainService namingMaintainService;

	public NamingService getNamingService(Properties properties) {
		if (Objects.isNull(this.namingService)) {
			buildNamingService(properties);
		}
		return namingService;
	}

	public NamingMaintainService getNamingMaintainService(Properties properties) {
		if (Objects.isNull(namingMaintainService)) {
			buildNamingMaintainService(properties);
		}
		//InetUtils
		return namingMaintainService;
	}

	public boolean isNacosDiscoveryInfoChanged(
			NacosDiscoveryProperties nacosDiscoveryProperties) {
		if (Objects.isNull(nacosDiscoveryPropertiesCache)
				|| this.nacosDiscoveryPropertiesCache.equals(nacosDiscoveryProperties)) {
			return false;
		}
		copyProperties(nacosDiscoveryProperties, nacosDiscoveryPropertiesCache);
		return true;
	}

	private NamingMaintainService buildNamingMaintainService(Properties properties) {
		if (Objects.isNull(namingMaintainService)) {
			synchronized (NacosServiceManager.class) {
				if (Objects.isNull(namingMaintainService)) {
					namingMaintainService = createNamingMaintainService(properties);
				}
			}
		}
		return namingMaintainService;
	}

	private NamingService buildNamingService(Properties properties) {
		if (Objects.isNull(namingService)) {
			synchronized (NacosServiceManager.class) {
				if (Objects.isNull(namingService)) {
					namingService = createNewNamingService(properties);
				}
			}
		}
		return namingService;
	}

	private NamingService createNewNamingService(Properties properties) {
		try {
			return createNamingService(properties);
		}
		catch (NacosException e) {
			throw new RuntimeException(e);
		}
	}

	private NamingMaintainService createNamingMaintainService(Properties properties) {
		try {
			return createMaintainService(properties);
		}
		catch (NacosException e) {
			throw new RuntimeException(e);
		}
	}

	public void nacosServiceShutDown() throws NacosException {
		this.namingService.shutDown();
		namingService = null;
		namingMaintainService = null;
	}



}
