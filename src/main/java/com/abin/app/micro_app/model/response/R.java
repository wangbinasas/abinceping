package com.abin.app.micro_app.model.response;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/7
 * @Desc: 基础响应类
 */
@Data
public class R<T> {

    private int code;

    private String msg;

    private T data;

    public static R fail(String msg) {
        R r = new R();
        r.setCode(100);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }

    public static R success() {
        R r = new R();
        r.setCode(0);
        return r;
    }

    public static R success(Object data) {
        R r = new R();
        r.setCode(0);
        r.setData(data);
        return r;
    }
}
