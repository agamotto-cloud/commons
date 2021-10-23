package org.agamotto.cloud.exception;

public enum SampleError implements BaseError<SampleError> {

    ERROR_SYS(99901, "系统异常"),
    ERROR_PARAM(99902, "参数错误"),
    ERROR_OTHER(99903, "参数错误"),

    ;

    private final AgamottoException agamottoException;


    SampleError(Integer code, String msg) {
        this.agamottoException = AgamottoBusinessException.build(code, msg);
        addException(this);
    }


    @Override
    public AgamottoException getException() {
        return agamottoException;
    }


}
