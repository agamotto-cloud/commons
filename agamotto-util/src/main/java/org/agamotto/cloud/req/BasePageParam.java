package org.agamotto.cloud.req;

import lombok.Data;

@Data
public class BasePageParam {
    private Long current = 1L;
    private Long size = 10L;
}
