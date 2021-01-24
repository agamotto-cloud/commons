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

package org.agamotto.cloud.nacos;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuhuangbin
 */
@Slf4j
public class NacosServiceManager {


	private final NacosProperties nacosProperties;
	private final NamingService naming;
	public NacosServiceManager(NacosProperties nacosProperties){
		this.nacosProperties = nacosProperties;
		try {

			this.naming = NamingFactory.createNamingService(nacosProperties.getServerAddr());
		} catch (NacosException e) {
			log.error("nacos出错,{}",e.getErrMsg());
			throw new RuntimeException(e);
		}
	}
	public NamingService getNacosService() {
		return naming;
	}
}
