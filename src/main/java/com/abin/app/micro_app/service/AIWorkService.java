package com.abin.app.micro_app.service;

import com.abin.app.micro_app.common.constant.WorkTypeEnum;
import com.abin.app.micro_app.common.exception.BusinessException;
import com.abin.app.micro_app.common.exception.InfException;
import com.abin.app.micro_app.common.util.RateLimiter;
import com.abin.app.micro_app.model.CreateAIWorkResultVO;
import com.abin.app.micro_app.model.UserWorkBO;
import com.abin.app.micro_app.model.request.*;
import com.abin.app.micro_app.model.response.*;
import com.abin.app.micro_app.proxy.ImgProxy;
import com.abin.app.micro_app.proxy.TranslateProxy;
import com.abin.app.micro_app.proxy.UserWorkProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/7
 * @Desc:
 */
@Slf4j
@Service
public class AIWorkService {

    private ThreadPoolExecutor createThreadPool = new ThreadPoolExecutor(30, 30, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    Map<String, R<GetCreateProgressResponse>> generatingTasks = Collections.synchronizedMap(new HashMap<>());

    RateLimiter createRateLimiter = new RateLimiter(100);

    private static final long DEFAULT_WIDTH = 1170L;
    private static final long DEFAULT_HEIGHT = 2532L;

    @Resource
    private TranslateProxy translateProxy;

    @Resource
    private ImgProxy imgProxy;

    @Resource
    private UserWorkProxy userWorkProxy;


    /**
     * 创建作品
     */
    public R<CreateAIPictureResponse> createAIPicture(CreateAIWorkRequest req) {
        if (StringUtils.isEmpty(req.getCreateDesc())
                || StringUtils.isEmpty(req.getCreateStyle())
                || StringUtils.isEmpty(req.getUsername())) {
            throw new BusinessException("参数错误");
        }
        if (!createRateLimiter.isAllowed(req.getUsername())) {
            throw new BusinessException("创建过于频繁,请稍后重试哦~");
        }
        //真实提交任务
        final String taskId = UUID.randomUUID().toString();
        doSubmitCreateTask(req, taskId);
        R<CreateAIPictureResponse> res = R.success(new CreateAIPictureResponse());
        res.getData().setTaskId(taskId);
        return res;
    }

    private void doSubmitCreateTask(CreateAIWorkRequest req, String taskId) {
        R<GetCreateProgressResponse> progressRes = R.success(new GetCreateProgressResponse());
        progressRes.getData().setVoList(new ArrayList<>());
        generatingTasks.put(taskId, progressRes);
        createThreadPool.execute(() -> {
            String createDesc = req.getCreateDesc();
            String createStyle = req.getCreateStyle();
            String username = req.getUsername();
            try {
                createDesc = translateProxy.translateChinese2English(createDesc);
            } catch (InfException e) {
                log.error("translate failed {} {}.", username, createDesc, e);
                progressRes.setCode(100);
                progressRes.setMsg("生成作品失败.");
                return;
            }
            try {
                List<String> aiImgUrlList = imgProxy.createAIImg(createStyle, createDesc, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                if (!CollectionUtils.isEmpty(aiImgUrlList)) {
                    progressRes.getData().setVoList(new ArrayList<>(aiImgUrlList.stream().map(CreateAIWorkResultVO::new).collect(Collectors.toList())));
                }
            } catch (InfException e) {
                log.error("create img failed {} {} {}.", username, createStyle, createDesc, e);
                progressRes.setCode(100);
                progressRes.setMsg("生成作品失败.");
            }
        });
    }

    /**
     * 获取创建进度
     */
    public R<GetCreateProgressResponse> getCreateProgress(GetCreateProgressRequest req) {
        String taskId = req.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            throw new BusinessException("参数错误");
        }
        R<GetCreateProgressResponse> progressR = generatingTasks.get(taskId);
        if (progressR == null) {
            throw new BusinessException("找不到任务");
        }
        return progressR;
    }

    /**
     * 生成作品分享码
     */
    public R<GenerateWorkShareCodeResponse> generateWorkShareCode(GenerateWorkShareCodeRequest req) {
        String username = req.getUsername();
        String workContent = req.getWorkContent();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(workContent)) {
            throw new BusinessException("参数错误");
        }
        String shareCode = genShareCode();
        List<UserWorkBO> userWorkBOList = new ArrayList<>();
        UserWorkBO userWorkBO = new UserWorkBO();
        userWorkBO.setUsername(username);
        userWorkBO.setWorkContent(workContent);
        userWorkBO.setWorkTypeEnum(WorkTypeEnum.AI_IMG);
        userWorkBO.setShareCode(shareCode);
        userWorkBO.setUseCount(0);
        userWorkBOList.add(userWorkBO);
        GenerateWorkShareCodeResponse response = new GenerateWorkShareCodeResponse();
        UserWorkBO historyWorkBO;
        try {
            historyWorkBO = userWorkProxy.getWorkByWorkContent(workContent);
        } catch (InfException e) {
            throw new BusinessException("查询作品失败.", e);
        }
        if (historyWorkBO != null) {
            response.setShareCode(historyWorkBO.getShareCode());
            return R.success(response);
        }
        try {
            userWorkProxy.saveWorks(userWorkBOList);
        } catch (InfException e) {
            throw new BusinessException("生成分享码失败", e);
        }
        response.setShareCode(shareCode);
        return R.success(response);
    }

    private String genShareCode() {
        int length = 10;
        StringBuilder key = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomInt = random.nextInt(36);
            if (randomInt < 10) {
                key.append(randomInt);
            } else {
                key.append((char) ('a' + (randomInt - 10)));
            }
        }
        return key.toString();
    }

    /**
     * 根据分享码搜索作品
     */
    public R<SearchWorkByShareCodeResponse> searchWorkByShareCode(SearchWorkByShareCodeRequest req) {
        String currentUsername = req.getUsername();
        String shareCode = req.getShareCode();
        if (StringUtils.isEmpty(shareCode) || StringUtils.isEmpty(currentUsername)) {
            throw new BusinessException("参数错误");
        }
        SearchWorkByShareCodeResponse response = new SearchWorkByShareCodeResponse();
        try {
            UserWorkBO workBO = userWorkProxy.getWorkByShareCode(shareCode);
            if (workBO == null) {
                throw new BusinessException("没有找到哦");
            }
            userWorkProxy.addWorkUseCount(shareCode);
            response.setWorkContent(workBO.getWorkContent());
            response.setUseCount(workBO.getUseCount());
        } catch (InfException e) {
            throw new BusinessException("搜索失败", e);
        }
        return R.success(response);
    }

    /**
     * 获取用户信息
     */
    public R<GetUserInfoResponse> getUserInfo(GetUserInfoRequest req) {
        String currentUsername = req.getUsername();
        if (StringUtils.isEmpty(currentUsername)) {
            throw new BusinessException("参数错误");
        }
        GetUserInfoResponse response = new GetUserInfoResponse();
        try {
            int userWorksShareCount = userWorkProxy.getUserWorksShareCount(currentUsername);
            int userWorksCount = userWorkProxy.getUserWorksCount(currentUsername);
            response.setWorkCount(userWorksCount);
            response.setUseCount(userWorksShareCount);
        } catch (InfException e) {
            throw new BusinessException("获取用户信息失败", e);
        }
        return R.success(response);
    }

    /**
     * 更新Token
     */
    public void refreshToken(String token) {
        imgProxy.setToken(token);
    }

    public Integer getTaskCount() {
        return generatingTasks.size();
    }
}
