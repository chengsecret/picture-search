package com.example.picturesearch.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.picturesearch.entity.Dataset;
import com.example.picturesearch.entity.Picture;
import com.example.picturesearch.mapper.CategoryMapper;
import com.example.picturesearch.mapper.DatasetMapper;
import com.example.picturesearch.mapper.PictureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * @ClassName EsClient
 * @Author liaoyuaner
 * @Date 2024/5/9 21:50
 * @Version 1.0
 **/
@Component
@Slf4j
public class EsClient {
    @Autowired
    ElasticsearchClient client;
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DatasetMapper datasetMapper;

    public List<String> similarSearch(List<Double> vector, int topN, String index, String category) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                    .index(index)
                    .query(query -> query
                            .bool(b -> b
                                    .must(m -> m.term(t -> t.field("tag").value(FieldValue.of(category))))
                                    .must(m->m.knn(k -> k
                                            .field("vector")
                                            .numCandidates(100)
                                            .queryVector(vector)))))
                    .size(topN));
        SearchResponse<EsUpload> response = client.search(searchRequest, EsUpload.class);
        return response.hits().hits().stream().map(hit -> {
            if (hit.source()== null) {
                return null;
            }
            return hit.source().getUrl();
        }).collect(Collectors.toList());
    }

    public void upload(String index) throws IOException {
        QueryWrapper<Dataset> queryWrapper = new QueryWrapper<Dataset>().eq("name", index);
        Dataset dataset = datasetMapper.selectOne(queryWrapper);
        if(dataset == null){
            log.info("数据集不存在");
            return;
        }
        QueryWrapper<Picture> wrapper = new QueryWrapper<>();
        wrapper.eq("dataset_id", dataset.getId());
        List<Picture> pictures = pictureMapper.selectList(wrapper);

        // 构建一个批量操作BulkOperation的集合
        List<BulkOperation> bulkOperations = new ArrayList<>();
        pictures.forEach(picture -> {
            EsUpload esUpload = new EsUpload();
            esUpload.setUrl(picture.getUrl());
            esUpload.setVector(restoreVector(picture.getVector()));
            esUpload.setImageId(String.valueOf(picture.getPictureId()));
            esUpload.setName(picture.getPictureName());
            ArrayList<String> tags = categoryMapper.selectSuperCategories(picture.getPictureId());
            esUpload.setTag(tags);
            System.out.println(esUpload);
            bulkOperations.add(new BulkOperation.Builder()
                    .create(d-> d.document(esUpload)
                            .id(String.valueOf(picture.getId()))
                            .index(index)).build());
        });

        BulkResponse response = client.bulk(e->e.index(index).operations(bulkOperations));
        System.out.println("es是否上传成功：" + !response.errors());
    }

    private static Double[] restoreVector(String vectorAsString) {
        // 使用StringTokenizer来分割字符串
        StringTokenizer tokenizer = new StringTokenizer(vectorAsString, "[], ");
        int count = tokenizer.countTokens();
        Double[] restoredVector = new Double[count];

        for (int i = 0; i < count; i++) {
            // 将分割后的字符串转换为double
            restoredVector[i] = Double.parseDouble(tokenizer.nextToken());
        }

        return restoredVector;
    }
}