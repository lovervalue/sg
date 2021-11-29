package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.DataAnalyze;
import com.sofn.bigdata.vo.DataAnalyzeVo;
import com.sofn.bigdata.vo.DataTreeVo;
import com.sofn.common.utils.PageUtils;

import java.util.Map;

public interface DataAnalyzeService extends IService<DataAnalyze> {

    Map<String,Object> createDataAnalyze(String dataid, String dataname, String datacomment);

    void deleteDataAnalyze(String id);

    Map<String,Object> updateDataAnalyze(String id);

    PageUtils<DataAnalyzeVo> listPage(Integer pageNo, Integer pageSize, String dataname);

    DataAnalyzeVo view(String id);
}
