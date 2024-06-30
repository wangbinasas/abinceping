package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: Wangbin02
 * @Date: 2024/6/20
 * @Desc:
 */
@Data
public class QuestionAndAnswer {

    private Integer id;

    private String questionUrl;

    private List<SelectButton> buttonList;

    @Data
    public static class SelectButton {
        private Integer id;
        private String title;

        private String answer;
    }
}