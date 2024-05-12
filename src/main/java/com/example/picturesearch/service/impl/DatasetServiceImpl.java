package com.example.picturesearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.picturesearch.dto.DatasetInfoDTO;
import com.example.picturesearch.entity.Dataset;
import com.example.picturesearch.mapper.CategoryMapper;
import com.example.picturesearch.mapper.DatasetMapper;
import com.example.picturesearch.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author liaoyuan
* @description 针对表【dataset】的数据库操作Service实现
* @createDate 2024-05-12 11:05:43
*/
@Service
public class DatasetServiceImpl extends ServiceImpl<DatasetMapper, Dataset>
    implements DatasetService{
    @Autowired
    DatasetMapper datasetMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public List<DatasetInfoDTO> getDatasources() {
        List<DatasetInfoDTO> res = new ArrayList<>();
        List<Dataset> datasets = datasetMapper.selectList(new QueryWrapper<>());
        datasets.forEach(dataset -> {
            DatasetInfoDTO datasetInfoDTO = new DatasetInfoDTO().setValue(dataset.getName()).setLabel(dataset.getName());
            ArrayList<String> categories = categoryMapper.selectSuperCategoriesByDatasetId(dataset.getId());
            datasetInfoDTO.setCategory(categories.stream().map(
                    category -> new DatasetInfoDTO.category().setName(category).setLabel(category)

            ).collect(Collectors.toList()));
            res.add(datasetInfoDTO);
        });
        return res;
    }
}




