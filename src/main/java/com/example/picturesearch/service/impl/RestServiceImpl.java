package com.example.picturesearch.service.impl;

import com.example.picturesearch.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
//        Map<String, String> requestMap = new HashMap<>();
//        requestMap.put("url", imagePath);

        // 发送POST请求
//        List<Double> response = restTemplate.postForObject(url, requestMap, List.class);
//        if (response != null) {
//            return response.toArray(new Double[0]);
//
//        }else {
//            return null;
//        }
        Double[] vector = new Double[512];
        for (int i = 0; i < 512; i++)
            vector[i] = Math.random();
        return vector;
    }
}