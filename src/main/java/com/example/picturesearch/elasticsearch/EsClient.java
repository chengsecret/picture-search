package com.example.picturesearch.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
}