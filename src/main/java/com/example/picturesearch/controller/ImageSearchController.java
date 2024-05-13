package com.example.picturesearch.controller;

import com.example.picturesearch.dto.DatasetInfoDTO;
import com.example.picturesearch.elasticsearch.EsClient;
import com.example.picturesearch.service.DatasetService;
import com.example.picturesearch.service.PictureService;
import com.example.picturesearch.utils.R;
import com.example.picturesearch.utils.ZipTools;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageSearchController {

    private final DatasetService datasetService;

    private final EsClient esClient;

    private final PictureService pictureService;

    // 1.1 获取数据集
    @GetMapping("/getDatasource")
    public R getDatasource() {
        List<DatasetInfoDTO> list = datasetService.getDatasources();
        return R.ok(list);
    }

    // 1.2 搜索图片接口（上传图片搜索）
    @PostMapping("/search1")
    public R searchImage(
            @RequestParam("topN") String topN,
            @RequestParam("datasource") String datasource,
            @RequestParam("category") String category,
            @RequestParam("picture") MultipartFile picture
    ) throws IOException {
        // TODO 图片转向量
        List<Double> vector = List.of(1.0,2.3,3.3);
        datasource = "test";
        // es 检索
        List<String> res = esClient.similarSearch(vector, Integer.parseInt(topN), datasource, category);
        return R.ok(res);
    }

    // 1.3 获取随机图片 API
    @GetMapping("/randPic")
    public R getRandomPicture() {
        // 应该由前端传来数据集名称
        String dataset = "coco";
        // 这里应该是调用服务层的方法来获取随机图片的URL
        List<String> url = pictureService.getRandomPicture(dataset);
        if (url== null || url.isEmpty()) {
            return R.failed("无该数据集");
        }
        return R.ok(url); // 示例返回值
    }

    // 1.4 随机图片搜索接口
    @PostMapping("/search2")
    public R searchRandomImage(
            @RequestParam("topN") String topN,
            @RequestParam("datasource") String datasource,
            @RequestParam("category") String category,
            @RequestParam("img") String img
    ) throws IOException {
        // TODO 图片转向量
        List<Double> vector = List.of(1.0,2.3,3.3);
        datasource = "test";
        // es 检索
        List<String> res = esClient.similarSearch(vector, Integer.parseInt(topN), datasource, category);
        return R.ok(res);
    }

    // 2. 上传数据集
    @PostMapping("/uploadDatasource")
    public R uploadDatasource(@RequestParam("file") MultipartFile datasource) {
        // 这里应该是调用服务层的方法来处理上传的数据集文件
        // 返回操作结果，例如 "上传成功"
        System.out.println("上传前。。。。。。");
        ZipTools.dealZip(datasource);
        return R.ok(null,"上传成功");
    }
}