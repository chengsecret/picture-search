package com.example.picturesearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.picturesearch.elasticsearch.EsUpload;
import com.example.picturesearch.entity.Picture;
import com.example.picturesearch.mapper.CategoryMapper;
import com.example.picturesearch.mapper.PictureCategoryMapper;
import com.example.picturesearch.mapper.PictureMapper;
import com.example.picturesearch.service.RestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EsTest
 * @Author liaoyuaner
 * @Date 2024/5/9 21:12
 * @Version 1.0
 **/
@SpringBootTest
public class EsTest {
    @Autowired
    ElasticsearchClient client;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    PictureCategoryMapper pictureCategoryMapper;
    @Autowired
    RestService restService;

    @Test
    void upload() throws IOException {
        List<Picture> pictures = pictureMapper.selectList(new QueryWrapper<>());
        String INDEX = "coco";

        // 构建一个批量操作BulkOperation的集合
        List<BulkOperation> bulkOperations = new ArrayList<>();
        pictures.forEach(picture -> {
            EsUpload esUpload = new EsUpload();
            esUpload.setUrl(picture.getUrl());
            esUpload.setVector(restService.getVector(picture.getUrl()));
            esUpload.setImageId(String.valueOf(picture.getPictureId()));
            esUpload.setName(picture.getPictureName());
            ArrayList<String> tags = categoryMapper.selectSuperCategories(picture.getPictureId(), 1);
            esUpload.setTag(tags);
            System.out.println(esUpload);
            bulkOperations.add(new BulkOperation.Builder().create(d-> d.document(esUpload).id(String.valueOf(picture.getId())).index(INDEX)).build());
        });

        BulkResponse response = client.bulk(e->e.index(INDEX).operations(bulkOperations));
        System.out.println(response.errors());
    }
}