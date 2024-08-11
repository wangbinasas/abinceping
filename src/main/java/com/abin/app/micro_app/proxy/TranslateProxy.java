package com.abin.app.micro_app.proxy;

import com.abin.app.micro_app.common.exception.InfException;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc: 翻译接口
 */
public interface TranslateProxy {
    /**
     * 将中文翻译成英文
     */
    String translateChinese2English(String content) throws InfException;
}
