package org.agamotto.cloud.exception;

/**
 * @author
 * @Time 2019/6/2 21:24
 */

public class AgamottoDefaultException extends AgamottoException {

    private String message;
    private Integer code;

    private AgamottoDefaultException(String message, Integer code) {
        super(code, message);
        this.code = code;
        this.message = message;
    }

    public static AgamottoException build(String message, Integer code) {
        return new AgamottoDefaultException(message, code);
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
