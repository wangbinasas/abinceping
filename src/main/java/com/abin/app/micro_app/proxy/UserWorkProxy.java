package com.abin.app.micro_app.proxy;

import com.abin.app.micro_app.common.exception.InfException;
import com.abin.app.micro_app.model.UserWorkBO;

import java.util.List;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc: 用户创作相关接口
 */
public interface UserWorkProxy {

    /**
     * 写入
     */
    void saveWorks(List<UserWorkBO> userImgBOList) throws InfException;

    /**
     * 根据分享码获取图片
     */
    UserWorkBO getWorkByShareCode(String shareCode) throws InfException;

    /**
     * 添加作品使用数量
     */
    void addWorkUseCount(String shareCode) throws InfException;

    /**
     * 获取用户作品分享总次数
     */
    int getUserWorksShareCount(String username) throws InfException;

    /**
     * 获取用户作品数量
     */
    int getUserWorksCount(String username) throws InfException;
}
