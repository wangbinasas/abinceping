package com.abin.app.micro_app.model.request;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/13
 * @Desc:
 */
@Data
public class GetCreateProgressRequest {

    private String taskId;

    private String username;
}
