package com.example.picturesearch.mapper;

import com.example.picturesearch.entity.Picture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liaoyuan
* @description 针对表【picture】的数据库操作Mapper
* @createDate 2024-05-08 23:20:36
* @Entity com.example.entity.Picture
*/
public interface PictureMapper extends BaseMapper<Picture> {

    void insertBatch(List<Picture> pictureCategories);

    String selectRandomPicture(String dataset);
}




