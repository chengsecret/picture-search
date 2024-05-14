package com.example.picturesearch.service.impl;

import cn.hutool.http.HttpUtil;
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
    @Override
    public Double[] getVector(String imagePath) {
        JSONObject params = JSONUtil.createObj();
        params.set("url", imagePath);
        String post = HttpUtil.post(url, params);
        System.out.println(post);
        // 返回512维向量
        return null;
    }


    @Data
    static class VectorResponse {
        private List<List<Double>> vector;

        public void setVector(List<List<Double>> vector) {
            this.vector = vector;
        }
    }

}