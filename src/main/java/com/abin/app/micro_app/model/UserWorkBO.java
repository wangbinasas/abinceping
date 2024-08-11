package com.abin.app.micro_app.model;

import com.abin.app.micro_app.common.constant.WorkTypeEnum;
import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/8
 * @Desc:
 */
@Data
public class UserWorkBO {

    /**
     * 用户username
     */
    private String username;

    /**
     * 分享码
     */
    private String shareCode;

    /**
     * 创作类型
     */
    private WorkTypeEnum workTypeEnum;

    /**
     * 工作内容
     */
    private String workContent;

    /**
     * 使用次数
     */
    private Integer useCount;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
