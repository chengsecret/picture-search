package com.example.picturesearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName DatasetInfoDTO
 * @Author liaoyuaner
 * @Date 2024/5/12 11:09
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class DatasetInfoDTO {
    String value;
    String label;
    List<category> category;

    @Data
    @Accessors(chain = true)
    public static class category{
        String name;
        String label;
    }
}