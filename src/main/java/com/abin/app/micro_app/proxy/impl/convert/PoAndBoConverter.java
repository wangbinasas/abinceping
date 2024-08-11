package com.abin.app.micro_app.proxy.impl.convert;

import com.abin.app.micro_app.common.constant.WorkTypeEnum;
import com.abin.app.micro_app.model.UserWorkBO;
import com.abin.app.micro_app.proxy.impl.db.po.UserWorkPO;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/11
 * @Desc:
 */
public class PoAndBoConverter {

    public static UserWorkBO po2BO(UserWorkPO po) {
        UserWorkBO bo = new UserWorkBO();
        bo.setUsername(po.getUserName());
        bo.setShareCode(po.getShareCode());
        bo.setWorkTypeEnum(WorkTypeEnum.valueOf(po.getType().intValue()));
        bo.setWorkContent(po.getWorkContent());
        bo.setUseCount(po.getUseCount());
        bo.setCreateTime(po.getCreateTime().getTime());
        bo.setUpdateTime(po.getUpdateTime().getTime());
        return bo;
    }
}
