package com.example.picturesearch.mapper;

import com.example.picturesearch.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.List;

/**
* @author liaoyuan
* @description 针对表【category】的数据库操作Mapper
* @createDate 2024-05-08 23:19:33
* @Entity com.example.entity.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {

    void insertBatch(List<Category> pictureCategories);

    ArrayList<String> selectSuperCategories(Long pictureId);
}




