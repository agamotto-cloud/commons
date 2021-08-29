package org.agamotto.cloud.req;

import lombok.Data;

@Data
public class BasePageParam {
    private Long current;
    private Long size;
}
