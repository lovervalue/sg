package com.sofn.bigdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sofn.bigdata.model.JsonAnalyze;
import com.sofn.bigdata.model.JsonAnalyze;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface JsonAnalyzeMapper extends BaseMapper<JsonAnalyze> {

    void deleteJsonAnalyze(String id);

    void saveJsonAnalyze(Map<String, Object> praMap);

    List<JsonAnalyze> getJsonAnalyzeList(@Param("businame") String businame);

    void updateJsonAnalyze(Map<String, Object> praMap);

}
