package org.agamotto.cloud.resp;


import lombok.Data;

@Data
public class Ret<T> {
    private Boolean success;
    private Integer statusCode;
    private String msg;
    private T data;

    Ret() {
    }


}
