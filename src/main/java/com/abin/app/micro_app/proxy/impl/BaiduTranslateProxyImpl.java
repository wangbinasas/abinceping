package com.abin.app.micro_app.proxy.impl;

import com.abin.app.micro_app.common.exception.InfException;
import com.abin.app.micro_app.common.util.JsonUtil;
import com.abin.app.micro_app.proxy.TranslateProxy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/7
 * @Desc:
 */
@Slf4j
@Service
public class BaiduTranslateProxyImpl implements TranslateProxy {

    /**
     * Token缓存
     */
    private static volatile String ACCESS_TOKEN_CACHE;

    /**
     * Token过期时间
     */
    private static volatile Long ACCESS_TOKEN_EXPIRE_TIME;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private static final String TRANSLATE_URL = "https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1";
    private static final String CLIENT_ID = "ccDtQnQXM1YFq3qEDDmwhnAf";
    private static final String CLIENT_SECRET = "D2zOYuflZgRU6qZXJGWdWYX7DH92j3E7";

    /**
     * 翻译
     */
    public String textTrans(String from, String to, String q) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("from", from);
            map.put("to", to);
            map.put("q", q);
//            map.put("termIds", termIds);
            String param = JsonUtil.toJson(map);
            String accessToken = getToken();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, param);
            Request request = new Request.Builder()
                    .url(TRANSLATE_URL + "?access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            String jsonString = response.body().string();
            AllTranslateResult allTranslateResult = JsonUtil.fromJson(jsonString, AllTranslateResult.class);
            List<SinlgeResult> transResult = allTranslateResult.getResult().getTrans_result();
            if (!CollectionUtils.isEmpty(transResult)) {
                return transResult.get(0).getDst();
            }
        } catch (Exception e) {
            log.error("trans failed {} {} {}.", from, to, q, e);
        }
        return null;
    }

    @Override
    public String translateChinese2English(String content) throws InfException {
        String result = textTrans("zh", "en", content);
        if (result == null) {
            throw new InfException("翻译失败.", null);
        }
        log.info("translate success {} {}.", content, result);
        return result;
    }

    /**
     * 获取Token
     */
    public String getToken() {
        if (ACCESS_TOKEN_CACHE != null && System.currentTimeMillis() < ACCESS_TOKEN_EXPIRE_TIME) {
            return ACCESS_TOKEN_CACHE;
        }
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + CLIENT_ID
                            + "&client_secret=" + CLIENT_SECRET
                            + "&grant_type=client_credentials")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            GetTokenResult getTokenResult = JsonUtil.fromJson(response.body().string(), GetTokenResult.class);
            String accessToken = getTokenResult.getAccess_token();
            ACCESS_TOKEN_CACHE = accessToken;
            //缓存过期时间5分钟
            ACCESS_TOKEN_EXPIRE_TIME = System.currentTimeMillis() + 5 * 60 * 1000L;
            return accessToken;

        } catch (Exception e) {
            log.error("get baidu translate token failed.", e);
        }
        return null;
    }

    public static void main(String[] args) throws InfException {
        new BaiduTranslateProxyImpl().translateChinese2English("你好");
    }

    @Data
    class AllTranslateResult {
        private TransResult result;
        private String log_id;
    }

    @Data
    class TransResult {
        private List<SinlgeResult> trans_result;
        private String from;
        private String to;
    }

    @Data
    class SinlgeResult {
        private String dst;
        private String src;
    }

    @Data
    class GetTokenResult {
        private String access_token;
    }
}
