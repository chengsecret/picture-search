package com.example.picturesearch.controller;

import com.example.picturesearch.dto.DatasetInfoDTO;
import com.example.picturesearch.dto.DatasetUploadDTO;
import com.example.picturesearch.elasticsearch.EsClient;
import com.example.picturesearch.service.DatasetService;
import com.example.picturesearch.service.PictureService;
import com.example.picturesearch.service.RestService;
import com.example.picturesearch.utils.R;
import com.example.picturesearch.utils.ZipTools;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ImageSearchController {

    private final DatasetService datasetService;

    private final EsClient esClient;

    private final PictureService pictureService;

    private final RestService restService;

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
        String url = ZipTools.uploadToMinIO(picture);
        List<Double> vector = List.of(restService.getVector(url));
        // es 检索
        List<String> res = esClient.similarSearch(vector, Integer.parseInt(topN), datasource, category);
        System.out.println(res);
        return R.ok(res);
    }

    // 1.3 获取随机图片 API
    @PostMapping("/randPic")
    public R getRandomPicture(
            @RequestParam("datasource") String datasource
    ) {
        // 应该由前端传来数据集名称
        // 这里应该是调用服务层的方法来获取随机图片的URL
        List<String> url = pictureService.getRandomPicture(datasource);
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
        List<Double> vector = List.of(restService.getVector(img));
        // es 检索
        List<String> res = esClient.similarSearch(vector, Integer.parseInt(topN), datasource, category);
        return R.ok(res);
    }

    // 2. 上传数据集
    @PostMapping("/uploadDatasource")
    @SneakyThrows
    public R uploadDatasource(@RequestParam("file") MultipartFile datasource) {
        // 这里应该是调用服务层的方法来处理上传的数据集文件
        // 返回操作结果，例如 "上传成功"
        DatasetUploadDTO datasetUploadDTO = ZipTools.dealZip(datasource);
        boolean success = pictureService.uploadPictures(datasetUploadDTO);
        if (success) {
            esClient.upload(datasetUploadDTO.getDatasetName());
        }else {
            return R.failed("上传失败");
        }
        return R.ok(null,"上传成功");
    }
}