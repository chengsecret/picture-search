package com.example.picturesearch.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName DatasetUploadDTO
 * @Author liaoyuaner
 * @Date 2024/5/14 11:32
 * @Version 1.0
 **/
@Data
public class DatasetUploadDTO {
    String datasetName;
    Set<String> categories;
    Map<String, List<String>> pictures;
}