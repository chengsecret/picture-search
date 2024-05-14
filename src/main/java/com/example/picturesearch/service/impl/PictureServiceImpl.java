package com.example.picturesearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.picturesearch.dto.DatasetUploadDTO;
import com.example.picturesearch.entity.Category;
import com.example.picturesearch.entity.Dataset;
import com.example.picturesearch.entity.Picture;
import com.example.picturesearch.entity.PictureCategory;
import com.example.picturesearch.mapper.CategoryMapper;
import com.example.picturesearch.mapper.DatasetMapper;
import com.example.picturesearch.mapper.PictureCategoryMapper;
import com.example.picturesearch.mapper.PictureMapper;
import com.example.picturesearch.service.PictureService;
import com.example.picturesearch.service.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
* @author liaoyuan
* @description 针对表【picture】的数据库操作Service实现
* @createDate 2024-05-08 23:20:36
*/
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService {
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    DatasetMapper datasetMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    RestService restService;
    @Autowired
    PictureCategoryMapper pictureCategoryMapper;

    @Override
    public List<String> getRandomPicture(String datasetName) {
        QueryWrapper<Dataset> wrapper = new QueryWrapper<>();
        wrapper.eq("name", datasetName);
        Dataset dataset = datasetMapper.selectOne(wrapper);
        if (dataset == null) {
            return null;
        }
        return pictureMapper.selectRandomPicture(String.valueOf(dataset.getId()));
    }

    @Override
    @Transactional
    public boolean uploadPictures(DatasetUploadDTO datasetUploadDTO) {
        QueryWrapper<Dataset> queryWrapper = new QueryWrapper<Dataset>().eq("name", datasetUploadDTO.getDatasetName());
        Dataset dataset = datasetMapper.selectOne(queryWrapper);
        if (null != dataset) {
            log.info("dataset already exists");
            return false;
        }
        dataset = new Dataset();
        dataset.setName(datasetUploadDTO.getDatasetName());
        datasetMapper.insert(dataset);
        dataset = datasetMapper.selectOne(queryWrapper);

        Dataset finalDataset = dataset;
        AtomicInteger id = new AtomicInteger(1);
        Map<String, Integer> categoryIdMap = new HashMap<>();
        List<Category> pictureCategories = new ArrayList<>();
        datasetUploadDTO.getCategories().forEach(category -> {
            categoryIdMap.put(category, id.get());
                pictureCategories.add(new Category(
                        null,
                        category,
                        category,
                        id.getAndIncrement(),
                        finalDataset.getId()
                ));
        });
        categoryMapper.insertBatch(pictureCategories);

        Map<String, List<String>> pictures = datasetUploadDTO.getPictures();
        AtomicLong pictureId = new AtomicLong(1);
        pictures.forEach((imageUrl, tagList) -> {
            Double[] vector = restService.getVector(imageUrl);
            String[] split = imageUrl.split("/");
            Picture picture = new Picture();
            picture.setPictureName(split[split.length - 1])
                    .setUrl(imageUrl)
                    .setPictureId(pictureId.getAndIncrement())
                    .setDatasetId(finalDataset.getId())
                    .setVector(Arrays.toString(vector));
            pictureMapper.insert(picture);

            List<PictureCategory> pictureTag = new ArrayList<>();
            tagList.forEach(tag -> {
                PictureCategory pictureCategory = new PictureCategory(
                        null,
                        categoryIdMap.get(tag),
                        (int) (pictureId.get() - 1));
                // 打印对象，或者可以选择不打印，以提高性能
                // System.out.println(pictureCategory);
                pictureTag.add(pictureCategory);
            });
            pictureCategoryMapper.insertBatch(pictureTag);
        });

        return true;
    }
}




