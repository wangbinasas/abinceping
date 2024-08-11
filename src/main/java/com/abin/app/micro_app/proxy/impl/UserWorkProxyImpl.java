package com.abin.app.micro_app.proxy.impl;

import com.abin.app.micro_app.common.exception.InfException;
import com.abin.app.micro_app.model.UserWorkBO;
import com.abin.app.micro_app.proxy.UserWorkProxy;
import com.abin.app.micro_app.proxy.impl.convert.PoAndBoConverter;
import com.abin.app.micro_app.proxy.impl.db.mapper.UserWorkPOMapper;
import com.abin.app.micro_app.proxy.impl.db.po.UserWorkPO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/11
 * @Desc:
 */
@Service
public class UserWorkProxyImpl implements UserWorkProxy {

    @Resource
    private UserWorkPOMapper userWorkPOMapper;

    @Override
    public void saveWorks(List<UserWorkBO> userImgBOList) throws InfException {
        if (CollectionUtils.isEmpty(userImgBOList)) {
            return;
        }
        try {
            List<UserWorkPO> userWorkPOList = new ArrayList<>();
            for (UserWorkBO bo : userImgBOList) {
                UserWorkPO po = new UserWorkPO();
                po.setUserName(bo.getUsername());
                po.setType((byte) bo.getWorkTypeEnum().getCode());
                po.setWorkContent(bo.getWorkContent());
                po.setShareCode(bo.getShareCode());
                po.setUseCount(bo.getUseCount());
                userWorkPOList.add(po);
            }
            userWorkPOMapper.batchInsert(userWorkPOList);
        } catch (Exception e) {
            throw new InfException("保存创作失败.", e);
        }
    }

    @Override
    public UserWorkBO getWorkByShareCode(String shareCode) throws InfException {
        if (StringUtils.isEmpty(shareCode)) {
            return null;
        }
        try {
            UserWorkPO userWorkPO = userWorkPOMapper.selectByShareCode(shareCode);
            return PoAndBoConverter.po2BO(userWorkPO);
        } catch (Exception e) {
            throw new InfException("根据分享码查询作品失败.", e);
        }
    }

    @Override
    public void addWorkUseCount(String shareCode) throws InfException {
        if (StringUtils.isEmpty(shareCode)) {
            return;
        }
        try {
            userWorkPOMapper.addWorkUseCount(shareCode);
        } catch (Exception e) {
            throw new InfException("添加作品热度失败.", e);
        }
    }

    @Override
    public int getUserWorksShareCount(String username) throws InfException {
        if (StringUtils.isEmpty(username)) {
            return 0;
        }
        try {
            Integer userWorkShareCount = userWorkPOMapper.getUserWorkShareCount(username);
            return userWorkShareCount == null ? 0 : userWorkShareCount;
        } catch (Exception e) {
            throw new InfException("查询个人作品总热度失败.", e);
        }
    }

    @Override
    public int getUserWorksCount(String username) throws InfException {
        if (StringUtils.isEmpty(username)) {
            return 0;
        }
        try {
            Integer userWorkCount = userWorkPOMapper.getUserWorkCount(username);
            return userWorkCount == null ? 0 : userWorkCount;
        } catch (Exception e) {
            throw new InfException("查询个人作品数量失败.", e);
        }
    }
}
