package com.example.picturesearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.picturesearch.entity.Category;
import com.example.picturesearch.mapper.CategoryMapper;
import com.example.picturesearch.service.CategoryService;
import org.springframework.stereotype.Service;

/**
* @author liaoyuan
* @description 针对表【category】的数据库操作Service实现
* @createDate 2024-05-08 23:19:33
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

}




