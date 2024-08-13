package com.abin.app.micro_app.model.request;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/7
 * @Desc:
 */
@Data
public class CreateAIWorkRequest {

    /**
     * 用户username
     */
    private String username;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 创作描述
     */
    private String createDesc;
    /**
     * 创作风格
     */
    private String createStyle;
}
