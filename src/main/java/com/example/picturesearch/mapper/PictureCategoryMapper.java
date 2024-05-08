package com.example.picturesearch.mapper;

import com.example.picturesearch.entity.PictureCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liaoyuan
* @description 针对表【picture_category】的数据库操作Mapper
* @createDate 2024-05-08 23:20:42
* @Entity com.example.entity.PictureCategory
*/
public interface PictureCategoryMapper extends BaseMapper<PictureCategory> {

    void insertBatch(List<PictureCategory> pictureCategories);
}




