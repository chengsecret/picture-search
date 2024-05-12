package com.example.picturesearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.picturesearch.entity.Dataset;
import com.example.picturesearch.entity.Picture;
import com.example.picturesearch.mapper.DatasetMapper;
import com.example.picturesearch.mapper.PictureMapper;
import com.example.picturesearch.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author liaoyuan
* @description 针对表【picture】的数据库操作Service实现
* @createDate 2024-05-08 23:20:36
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService {
    @Autowired
    PictureMapper pictureMapper;
    @Autowired
    DatasetMapper datasetMapper;
    @Override
    public String getRandomPicture(String datasetName) {
        QueryWrapper<Dataset> wrapper = new QueryWrapper<>();
        wrapper.eq("name", datasetName);
        Dataset dataset = datasetMapper.selectOne(wrapper);
        if (dataset == null) {
            return null;
        }
        return pictureMapper.selectRandomPicture(String.valueOf(dataset.getId()));
    }
}




