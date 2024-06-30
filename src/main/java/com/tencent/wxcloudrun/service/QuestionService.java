package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.QuestionAndAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: Wangbin02
 * @Date: 2024/6/20
 * @Desc:
 */
@Service
public class QuestionService {

    @Resource
    private QuestionProxy questionProxy;

    /**
     * 查询问题
     */
    public QuestionAndAnswer queryById(Integer id) {
        QuestionAndAnswer questionAndAnswer = questionProxy.getById(id);
        return questionAndAnswer;
    }
}
