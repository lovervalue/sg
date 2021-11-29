package com.sofn.bigdata.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.BusinessSource;
import com.sofn.bigdata.model.BusinessSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BusinessSourceMapper extends BaseMapper<BusinessSource> {

    void saveBusinessSource(Map<String, Object> praMap);

    List<BusinessSource> getBusinessSourceList(@Param("businame") String businame);

    void updateBusinessSource(Map<String, Object> praMap);

    void deleteBusinessSource(String id);

}
