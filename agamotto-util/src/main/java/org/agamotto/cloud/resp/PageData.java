package org.agamotto.cloud.resp;


import lombok.Data;

import java.util.List;

@Data
public class PageData<D> {
    private Long current;
    private Long pages;
    private Long size;
    private Long total;
    private List<D> data;
}
