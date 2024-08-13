package com.abin.app.micro_app.proxy.impl;

import com.abin.app.micro_app.common.exception.InfException;
import com.abin.app.micro_app.proxy.ImgProxy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/9
 * @Desc:
 */
@Slf4j
@Service
public class ImgProxyImpl implements ImgProxy {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    /**
     * 全局Http对象
     */
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // 连接超时时间
            .readTimeout(60, TimeUnit.SECONDS) // 读取超时时间
            .writeTimeout(60, TimeUnit.SECONDS) // 写入超时时间
            .build();
    private static final String INPUT_MODE = "gen2";
    private static final String TEAM_ID = "10916506";
    private static final String HARD_WARE = "gpu";
    private static final String PRIORITY = "high";
    private static String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTA5MTY1MDYsImVtYWlsIjoid2FuZ2JpbmFzYXNAZ21haWwuY29tIiwiZXhwIjoxNzIzMzU4NTA1LjIyNywiaWF0IjoxNzIzMzU3OTA1LjIyNywic3NvIjpmYWxzZX0.piBUTPiZlst9TJuQu5lVqWtU0WSMej8zcdFf31jq_Jc";

    /**
     * 创作图片
     *
     * @param style  风格
     * @param desc   图片描述
     * @param length 长度
     * @param height 宽度
     * @return 图片地址
     */
    @Override
    public List<String> createAIImg(String style, String desc, Long length, Long height) throws InfException {
        List<String> result = Collections.synchronizedList(new ArrayList<>());
        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            futureList.add(threadPoolExecutor.submit(() -> {
                Request request = new Request.Builder()
                        .url(buildUrl(style, desc, length, height))
                        .get()
                        .build();
                try {
                    Response response = OK_HTTP_CLIENT.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String imgUrl = response.body().string();
                        result.add(imgUrl);
                        log.info("create img success {} {} {}.", style, desc, imgUrl);
                    } else {
                        log.error("🆘 create img failed. {} {} {} {}", style, desc, response.code(), response.body().string());
                        throw new RuntimeException("创作失败,Token过期");
                    }
                } catch (IOException e) {
                    log.error("create img exception.", e);
                }
            }));
        }
        try {
            for (Future future : futureList) {
                future.get();
            }
        } catch (Exception e) {
            throw new InfException("生成图片失败", e);
        }
        return result;
    }


    /**
     * 更新Token
     */
    public void setToken(String token) {
        TOKEN = token;
    }

    private String buildUrl(String style, String desc, Long width, Long height) {
        String sb = "https://streaming-inference.models.runwayml.cloud/streams-server-queue/gen1/commands/preview_to_url/result.txt?" +
                "input_text_prompt=" + desc +
                "&input_seed=" + (new Random().nextInt(1000000)) +
                "&input_mode=" + INPUT_MODE +
                "&input_style=" + style +
                "&input_width=" + width +
                "&input_height=" + height +
                "&input_api_jwt_token=" + TOKEN +
                "&as_team_id=" + TEAM_ID +
                "&hardware=" + HARD_WARE +
                "&priority=" + PRIORITY +
                "&tok=" + TOKEN;
        return sb;
    }

    public static void main(String[] args) throws InterruptedException, InfException {
        List<String> aiImg = new ImgProxyImpl().createAIImg("cinematic", "Chinese Girl", 1366L, 768L);
        System.out.println(aiImg);
    }
}
