package org.agamotto.cloud.resp;


import com.baomidou.mybatisplus.core.metadata.IPage;

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

    public static <T> Ret<PageData<T>> ok(IPage<T> data) {
        Ret<PageData<T>> ret = new Ret<>();
        PageData<T> pageData = new PageData<>();
        pageData.setCurrent(data.getCurrent());
        pageData.setPages(data.getPages());
        pageData.setSize(data.getSize());
        pageData.setTotal(data.getTotal());
        pageData.setData(data.getRecords());
        ret.setData(pageData);
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
