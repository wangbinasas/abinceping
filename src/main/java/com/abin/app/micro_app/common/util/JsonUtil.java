package com.abin.app.micro_app.common.util;

import com.google.gson.Gson;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/11
 * @Desc:
 */
public class JsonUtil {

    private static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> c) {
        return gson.fromJson(json, c);
    }
}
