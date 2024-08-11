package com.abin.app.micro_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/7
 * @Desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAIWorkResultVO {

    /**
     * 工作内容
     */
    private String workContent;
}
