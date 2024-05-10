package com.example.picturesearch.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageSearchController {

    // 1.1 获取数据集
    @GetMapping("/getDatasource")
    public List<?> getDatasource() {
        // 这里应该是调用服务层的方法来获取数据集
        // 由于没有具体的业务逻辑，这里仅返回一个示例JSON数组
        return List.of(
                Map.of(
                        "value", "animals",
                        "label", "动物",
                        "category", List.of(
                                Map.of("label", "cat", "name", "猫"),
                                Map.of("label", "dog", "name", "狗"),
                                Map.of("label", "bird", "name", "鸟")
                        )
                ),
                Map.of(
                        "value", "humans",
                        "label", "人类",
                        "category", List.of(
                                Map.of("label", "child", "name", "儿童"),
                                Map.of("label", "young", "name", "年轻人"),
                                Map.of("label", "oldman", "name", "老人")
                        )
                )
        );
    }

    // 1.2 搜索图片接口（上传图片搜索）
    @PostMapping("/search1")
    public List<String> searchImage(
            @RequestParam("topN") String topN,
            @RequestParam("datasource") String datasource,
            @RequestParam("category") String category,
            @RequestParam("picture") MultipartFile picture
    ) {
        // 这里应该是调用服务层的方法来根据上传的图片进行搜索
        // 返回搜索结果的图片URL列表
        return List.of("图片URL1", "图片URL2"); // 示例返回值
    }

    // 1.3 获取随机图片 API
    @GetMapping("/randPic")
    public List<String> getRandomPicture() {
        // 这里应该是调用服务层的方法来获取随机图片的URL
        return List.of("随机图片URL1", "随机图片URL2"); // 示例返回值
    }

    // 1.4 随机图片搜索接口
    @PostMapping("/search2")
    public List<String> searchRandomImage(
            @RequestParam("topN") String topN,
            @RequestParam("datasource") String datasource,
            @RequestParam("category") String category,
            @RequestParam("img") String img
    ) {
        // 这里应该是调用服务层的方法来进行随机图片搜索
        return List.of("搜索结果图片URL1", "搜索结果图片URL2"); // 示例返回值
    }

    // 2. 上传数据集
    @PostMapping("/uploadDatasource")
    public String uploadDatasource(@RequestParam("datasource") MultipartFile datasource) {
        // 这里应该是调用服务层的方法来处理上传的数据集文件
        // 返回操作结果，例如 "上传成功"
        return "上传成功";
    }
}