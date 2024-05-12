package com.example.picturesearch.service;

import com.example.picturesearch.dto.DatasetInfoDTO;
import com.example.picturesearch.entity.Dataset;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liaoyuan
* @description 针对表【dataset】的数据库操作Service
* @createDate 2024-05-12 11:05:43
*/
public interface DatasetService extends IService<Dataset> {

    List<DatasetInfoDTO> getDatasources();
}
