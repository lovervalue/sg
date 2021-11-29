package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.BusiData;

import java.util.Map;

public interface BusiDataMapper extends BaseMapper<BusiData> {

    void saveBusiData(Map<String,Object> params);

    void deleteBusiData(String id);

}
