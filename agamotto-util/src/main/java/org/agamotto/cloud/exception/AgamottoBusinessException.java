package org.agamotto.cloud.exception;

/**
 * @author
 * @Time 2019/6/2 21:24
 */

public class AgamottoBusinessException extends AgamottoException {

    private String message;
    private Integer code;

    private AgamottoBusinessException( Integer code,String message) {
        super( code,message);
        this.code = code;
        this.message = message;
    }

    public static AgamottoException build(Integer code,String message ) {
        return new AgamottoBusinessException(code,message);
    }


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
