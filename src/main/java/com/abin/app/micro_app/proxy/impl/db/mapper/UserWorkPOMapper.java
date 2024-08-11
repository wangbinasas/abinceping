package com.abin.app.micro_app.proxy.impl.db.mapper;

import com.abin.app.micro_app.proxy.impl.db.po.UserWorkPO;
import org.apache.ibatis.annotations.Param;

public interface UserWorkPOMapper {
    int batchInsert(@Param("list") java.util.List<UserWorkPO> list);

    UserWorkPO selectByShareCode(@Param("shareCode") String shareCode);

    void addWorkUseCount(@Param("shareCode") String shareCode);

    Integer getUserWorkShareCount(@Param("username") String username);

    Integer getUserWorkCount(@Param("username") String username);
}