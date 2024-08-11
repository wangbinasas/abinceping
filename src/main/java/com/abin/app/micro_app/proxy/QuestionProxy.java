package com.abin.app.micro_app.proxy;

import com.abin.app.micro_app.model.QuestionAndAnswer;

/**
 * @Author: Wangbin02
 * @Date: 2024/6/20
 * @Desc: 问题 Proxy
 */
public interface QuestionProxy {
    QuestionAndAnswer getById(Integer id);
}
