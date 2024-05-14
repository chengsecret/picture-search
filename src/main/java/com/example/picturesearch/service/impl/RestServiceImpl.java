package com.example.picturesearch.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.picturesearch.service.RestService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName RestServiceImpl
 * @Author liaoyuaner
 * @Date 2024/5/14 12:56
 * @Version 1.0
 **/
@Service
public class RestServiceImpl implements RestService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${model.url}")
    String url;

    public Double[] getVector(String imagePath) {
        JSONObject params = JSONUtil.createObj();
        params.set("url", imagePath);
        String body = HttpUtil.createPost(url)
                .contentType("application/json")
                .body(params.toString()).execute().body();

        JSONObject jsonObject = new JSONObject(body);
        // 提取vector数组
        JSONArray vectorArray = jsonObject.getJSONArray("vector");
        // 获取数组的第一个元素，也是一个数组
        JSONArray doubleArray = vectorArray.getJSONArray(0);
        Double[] vectors = new Double[doubleArray.size()];
        for (int i = 0; i < doubleArray.size(); i++) {
            vectors[i] = doubleArray.getDouble(i);
        }
        // 返回512维向量
        return vectors;
    }


    @Data
    static class VectorResponse {
        private List<List<Double>> vector;

        public void setVector(List<List<Double>> vector) {
            this.vector = vector;
        }
    }

}