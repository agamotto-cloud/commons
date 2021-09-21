package org.agamotto.cloud.status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Status {
    private String name;
    private Integer status;
}