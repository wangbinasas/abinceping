package com.abin.app.micro_app.model.request;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc:
 */
@Data
public class GenerateWorkShareCodeRequest {
    /**
     * 用户username
     */
    private String username;

    /**
     * 创作内容
     */
    private String workContent;

}
