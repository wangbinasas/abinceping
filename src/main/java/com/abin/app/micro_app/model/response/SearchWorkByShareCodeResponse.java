package com.abin.app.micro_app.model.response;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc:
 */
@Data
public class SearchWorkByShareCodeResponse {
    private String workContent;

    private Integer useCount;
}
