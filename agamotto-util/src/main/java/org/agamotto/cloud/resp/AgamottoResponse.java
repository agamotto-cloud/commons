package org.agamotto.cloud.resp;


public class AgamottoResponse {




    public static <T> Ret<T> ok() {
        Ret<T> ret = new Ret<>();
        ret.setMsg("ok");
        ret.setSuccess(true);
        ret.setStatusCode(200);
        return ret;
    }

    public static <T> Ret<T> ok(T data) {
        Ret<T> ret = new Ret<>();
        ret.setData(data);
        ret.setMsg("ok");
        ret.setSuccess(true);
        ret.setStatusCode(200);
        return ret;
    }

    public static <T> Ret<T> ok(T data, String msg) {
        Ret<T> ret = new Ret<>();
        ret.setData(data);
        ret.setMsg(msg);
        ret.setSuccess(true);
        ret.setStatusCode(200);
        return ret;
    }

    public static <T> Ret<T> error(int code) {
        Ret<T> ret = new Ret<>();
        ret.setMsg("error");
        ret.setSuccess(false);
        ret.setStatusCode(code);
        return ret;
    }

    public static <T> Ret<T> error(int code, String msg) {
        Ret<T> ret = new Ret<>();
        ret.setMsg(msg);
        ret.setSuccess(false);
        ret.setStatusCode(code);
        return ret;
    }


}
