package com.example.picturesearch.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @ClassName EsUpload
 * @Author liaoyuaner
 * @Date 2024/5/9 22:08
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsUpload {
    private String imageId;
    private String name;
    private String url;
    private Double[] vector;
    private ArrayList<String> tag;
}