package com.abin.app.micro_app.controller;

import com.abin.app.micro_app.model.QuestionAndAnswer;
import com.abin.app.micro_app.model.request.QueryQuestionRequest;
import com.abin.app.micro_app.service.QuestionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: Wangbin02
 * @Date: 2024/6/20
 * @Desc:
 */
@RestController
@RequestMapping("/question")
@CrossOrigin
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @RequestMapping(value = "/queryQuestions")
    public QuestionAndAnswer queryQuestions(@RequestBody QueryQuestionRequest request) {
        Integer requestId = request.getId();
        return questionService.queryById(requestId);
    }


}
