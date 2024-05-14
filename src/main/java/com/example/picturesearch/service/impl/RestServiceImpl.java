package com.example.picturesearch.service.impl;

import com.example.picturesearch.service.RestService;
import org.springframework.stereotype.Service;

/**
 * @ClassName RestServiceImpl
 * @Author liaoyuaner
 * @Date 2024/5/14 12:56
 * @Version 1.0
 **/
@Service
public class RestServiceImpl implements RestService {
    @Override
    public Double[] getVector(String imagePath) {
        Double[] vector = new Double[512];
        for (int i = 0; i < 512; i++)
            vector[i] = Math.random();
        return vector;
    }
}