package com.abin.app.micro_app.common.constant;

import java.util.Objects;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc: 创作类型枚举
 */
public enum WorkTypeEnum {

    AI_IMG(1, "AI图片");

    private final int code;

    private final String desc;

    WorkTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WorkTypeEnum valueOf(Integer code) {
        WorkTypeEnum[] values = WorkTypeEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (Objects.equals(values[i].getCode(), code)) {
                return values[i];
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
