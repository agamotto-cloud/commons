package org.agamotto.cloud.status;

import lombok.Builder;
import lombok.Data;

/**
 * 统一管理状态
 */
public interface BaseStatus<T extends BaseStatus<?>> {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseStatus.class);

    Integer value();

    Status getStatus();





}
