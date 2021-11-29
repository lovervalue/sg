package com.sofn.bigdata.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.DataShare;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataShareMapper extends BaseMapper<DataShare> {

    void saveDataShare(Map<String, Object> praMap);

    List<DataShare> getDataShareList(@Param("interName") String interName,@Param("interStatus") String interStatus);

    void updateDataShare(Map<String, Object> praMap);

    void deleteDataShare(String id);

    List<DataShare> getDataShareListByInterid(@Param("interId") String interId);
}
