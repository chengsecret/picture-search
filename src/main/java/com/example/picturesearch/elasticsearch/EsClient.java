package com.example.picturesearch.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName EsClient
 * @Author liaoyuaner
 * @Date 2024/5/9 21:50
 * @Version 1.0
 **/
@Component
public class EsClient {
    @Autowired
    ElasticsearchClient client;


}