package org.agamotto.cloud.exception;

/**
 * @author
 * @Time 2019/6/2 21:24
 */
public abstract class AgamottoException extends RuntimeException {



    public AgamottoException( Integer code , String message) {
        super(message);
        setCode(code);
        setMessage(message);
    }


    public abstract Integer getCode();

    public abstract void setCode(Integer code);

    public abstract String getMessage();

    public abstract void setMessage(String message);

}
