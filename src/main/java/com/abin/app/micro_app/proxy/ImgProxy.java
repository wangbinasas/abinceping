package com.abin.app.micro_app.proxy;

import com.abin.app.micro_app.common.exception.InfException;

import java.util.List;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/8
 * @Desc: 图片相关接口
 */
public interface ImgProxy {

    /**
     * 创作AI图片
     *
     * @param style  图片风格
     * @param desc   图片表述
     * @param width  图片长度
     * @param height 图片宽度
     */
    List<String> createAIImg(String style, String desc, Long width, Long height) throws InfException;

    /**
     * 更新Token
     *
     * @param token token
     */
    void setToken(String token);
}
