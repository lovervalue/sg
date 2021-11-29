package com.sofn.bigdata.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.DataAnalyze;
import com.sofn.bigdata.model.DataAnalyze;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DataAnalyzeMapper extends BaseMapper<DataAnalyze> {

    DataAnalyze view(String id);

    void deleteDataAnalyze(String id);

    void saveDataAnalyze(Map<String, Object> praMap);

    List<DataAnalyze> getDataAnalyzeList(@Param("businame") String businame);

    void updateDataAnalyze(Map<String, Object> praMap);

}
