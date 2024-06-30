package com.tencent.wxcloudrun.service;


import com.tencent.wxcloudrun.model.QuestionAndAnswer;

/**
 * @Author: Wangbin02
 * @Date: 2024/6/20
 * @Desc: 问题 Proxy
 */
public interface QuestionProxy {
    QuestionAndAnswer getById(Integer id);
}
