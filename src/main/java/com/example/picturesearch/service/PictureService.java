package com.example.picturesearch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.picturesearch.entity.Picture;

import java.util.List;

/**
* @author liaoyuan
* @description 针对表【picture】的数据库操作Service
* @createDate 2024-05-08 23:20:36
*/
public interface PictureService extends IService<Picture> {

    List<String> getRandomPicture(String dataset);
}
