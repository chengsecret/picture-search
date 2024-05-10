package com.example.picturesearch.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName EsData
 * @Author liaoyuaner
 * @Date 2024/5/9 21:46
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    String imageId;
    String name;
    String url;
}