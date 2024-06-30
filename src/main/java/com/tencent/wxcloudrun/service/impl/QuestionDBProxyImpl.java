package com.tencent.wxcloudrun.service.impl;

import com.google.gson.Gson;
import com.tencent.wxcloudrun.model.QuestionAndAnswer;
import com.tencent.wxcloudrun.service.QuestionProxy;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wangbin02
 * @Date: 2024/6/20
 * @Desc:
 */
@Service
public class QuestionDBProxyImpl implements QuestionProxy {

    private String filePath = "./db.txt";

    Map<Integer, QuestionAndAnswer> QAA_CACHE = new HashMap<>();

    private Gson gson = new Gson();

    @Override
    public QuestionAndAnswer getById(Integer id) {
        loadCache();
        return QAA_CACHE.get(id);
    }

    private void loadCache() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("db.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("读到的数据:" + line);
                QuestionAndAnswer questionAndAnswer = gson.fromJson(line, QuestionAndAnswer.class);
                QAA_CACHE.put(questionAndAnswer.getId(), questionAndAnswer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
