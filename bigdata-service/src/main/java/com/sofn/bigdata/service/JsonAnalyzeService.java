package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.JsonAnalyze;
import com.sofn.bigdata.vo.JsonAnalyzeVo;
import com.sofn.common.utils.PageUtils;

import java.util.Map;

public interface JsonAnalyzeService extends IService<JsonAnalyze> {

    void createJsonAnalyze(JsonAnalyzeVo jsonAnalyzeVo);

    void deleteJsonAnalyze(String id);

    void updateJsonAnalyze(JsonAnalyzeVo jsonAnalyzeVo);

    PageUtils<JsonAnalyzeVo> listPage(Integer pageNo, Integer pageSize, String dataname);

}
