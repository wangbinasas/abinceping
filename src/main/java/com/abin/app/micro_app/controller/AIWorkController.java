package com.abin.app.micro_app.controller;

import com.abin.app.micro_app.common.exception.BusinessException;
import com.abin.app.micro_app.model.CreateAIWorkResultVO;
import com.abin.app.micro_app.model.request.CreateAIWorkRequest;
import com.abin.app.micro_app.model.request.GenerateWorkShareCodeRequest;
import com.abin.app.micro_app.model.request.GetUserInfoRequest;
import com.abin.app.micro_app.model.request.SearchWorkByShareCodeRequest;
import com.abin.app.micro_app.model.response.GenerateWorkShareCodeResponse;
import com.abin.app.micro_app.model.response.GetUserInfoResponse;
import com.abin.app.micro_app.model.response.R;
import com.abin.app.micro_app.model.response.SearchWorkByShareCodeResponse;
import com.abin.app.micro_app.service.AIWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public R<List<CreateAIWorkResultVO>> createAIPicture(@RequestBody CreateAIWorkRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("openid");
            req.setUsername(openid);
            log.info("获得的openId:{}.", openid);
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
     * 生成作品分享码
     */
    @PostMapping(value = "/generateWorkShareCode")
    public R<GenerateWorkShareCodeResponse> generateWorkShareCode(@RequestBody GenerateWorkShareCodeRequest req, HttpServletRequest servletRequest) {
        try {
            String openid = servletRequest.getHeader("openid");
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
            String openid = servletRequest.getHeader("openid");
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
            String openid = servletRequest.getHeader("openid");
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
}
