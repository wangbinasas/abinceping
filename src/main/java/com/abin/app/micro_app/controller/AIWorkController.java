package com.abin.app.micro_app.controller;

import com.abin.app.micro_app.common.exception.BusinessException;
import com.abin.app.micro_app.model.request.*;
import com.abin.app.micro_app.model.response.*;
import com.abin.app.micro_app.service.AIWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/5
 * @Desc:
 */
@Slf4j
@RestController
@RequestMapping("/aiwork")
@CrossOrigin
public class AIWorkController {

    @Resource
    private AIWorkService aiWorkService;

    /**
     * 创建作品
     */
    @PostMapping(value = "/createAIPicture")
    public R<CreateAIPictureResponse> createAIPicture(@RequestBody CreateAIWorkRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("x-wx-openid");
            req.setUsername(openid);
            return aiWorkService.createAIPicture(req);
        } catch (BusinessException e) {
            log.error("🐸 create ai picture business exception, {}.", req, e);
            return R.fail(e.getMsg());
        } catch (Exception e) {
            log.error("❌ create ai picture error, {}.", req, e);
            return R.fail("网络异常,请稍后重试");
        }
    }

    /**
     * 创建作品
     */
    @PostMapping(value = "/getCreateProgress")
    public R<GetCreateProgressResponse> getCreateProgress(@RequestBody GetCreateProgressRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("x-wx-openid");
            req.setUsername(openid);
            return aiWorkService.getCreateProgress(req);
        } catch (BusinessException e) {
            log.error("🐸 get create progress business exception, {}.", req, e);
            return R.fail(e.getMsg());
        } catch (Exception e) {
            log.error("❌ get create progress error, {}.", req, e);
            return R.fail("网络异常,请稍后重试");
        }
    }

    /**
     * 生成作品分享码
     */
    @PostMapping(value = "/generateWorkShareCode")
    public R<GenerateWorkShareCodeResponse> generateWorkShareCode(@RequestBody GenerateWorkShareCodeRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("x-wx-openid");
            req.setUsername(openid);
            return aiWorkService.generateWorkShareCode(req);
        } catch (BusinessException e) {
            log.error("🐸 generate work share code business exception, {}.", req, e);
            return R.fail(e.getMsg());
        } catch (Exception e) {
            log.error("❌ generate work share code error, {}.", req, e);
            return R.fail("网络异常,请稍后重试");
        }
    }

    /**
     * 根据分享码搜索作品
     */
    @PostMapping(value = "/searchWorkByShareCode")
    public R<SearchWorkByShareCodeResponse> searchWorkByShareCode(@RequestBody SearchWorkByShareCodeRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("x-wx-openid");
            req.setUsername(openid);
            return aiWorkService.searchWorkByShareCode(req);
        } catch (BusinessException e) {
            log.error("🐸 search work by share code business exception, {}.", req, e);
            return R.fail(e.getMsg());
        } catch (Exception e) {
            log.error("❌ search work by share code error, {}.", req, e);
            return R.fail("网络异常,请稍后重试");
        }
    }

    /**
     * 获取用户信息
     */
    @PostMapping(value = "/getUserInfo")
    public R<GetUserInfoResponse> getUserInfo(@RequestBody GetUserInfoRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("x-wx-openid");
            req.setUsername(openid);
            return aiWorkService.getUserInfo(req);
        } catch (BusinessException e) {
            log.error("🐸 get user info business exception, {}.", req, e);
            return R.fail(e.getMsg());
        } catch (Exception e) {
            log.error("❌ get user info error, {}.", req, e);
            return R.fail("网络异常,请稍后重试");
        }
    }

    /**
     * 刷新Token
     */
    @GetMapping("/freshToken")
    public String freshToken(@RequestParam("token") String token) {
        if (StringUtils.isEmpty(token)) {
            return "参数为空";
        }
        aiWorkService.refreshToken(token);
        return "success";
    }

    /**
     * 获取任务量
     */
    @GetMapping("/getCreateSize")
    public String getCreateSize() {
        return aiWorkService.getTaskCount().toString();
    }
}
