package org.agamotto.cloud.exception;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum SampleError {

    ERROR_SYS(99901, "系统异常"),
    ERROR_PARAM(99902, "参数错误"),
  //  ERROR_OTHER(99902, "参数错误"),

    ;

    private AgamottoException agamottoException;
    private Integer code;
    private String msg;

    SampleError(Integer code, String msg) {
        this.agamottoException = AgamottoBusinessException.build(code, msg);
        this.code = code;
        this.msg = msg;
    }

    private static final Map<Integer, AgamottoException> mappings;

    static {
        mappings = new HashMap<>(values().length);
        for (SampleError error : values()) {
            if (mappings.containsKey(error.code)) {
                throw new RuntimeException("异常" + error.code + "(" + error.msg + ")定义重复");
            }
            mappings.put(error.code, error.agamottoException);
        }
    }


    public void toThrow() {
        throw agamottoException;
    }

    @Nullable
    public static AgamottoException resolve(@Nullable Integer code) {
        return (code != null ? mappings.get(code) : null);
    }


}