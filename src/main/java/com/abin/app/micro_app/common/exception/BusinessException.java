package com.abin.app.micro_app.common.exception;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc:
 */
@Data
public class BusinessException extends RuntimeException {

    /**
     * 异常信息
     */
    private String msg;

    public BusinessException(String msg) {
        this.msg = msg;
    }

    public BusinessException(String msg, Exception e) {
        super(e);
        this.msg = msg;
    }
}
