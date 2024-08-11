package com.abin.app.micro_app.common.exception;

import lombok.Data;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc:
 */
@Data
public class InfException extends Exception {

    private String msg;

    public InfException(String msg) {
        this.msg = msg;
    }

    public InfException(String msg, Exception e) {
        super(e);
        this.msg = msg;
    }
}
