package com.abin.app.micro_app.model.response;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc:
 */
@Data
public class GetUserInfoResponse {

    /**
     * 作品数量
     */
    private Integer workCount;

    /**
     * 作品使用数量
     */
    private Integer useCount;
}
