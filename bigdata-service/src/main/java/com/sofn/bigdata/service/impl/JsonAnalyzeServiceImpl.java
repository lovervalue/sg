package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.mapper.BusinessSourceMapper;
import com.sofn.bigdata.mapper.JsonAnalyzeMapper;
import com.sofn.bigdata.model.BusinessSource;
import com.sofn.bigdata.model.JsonAnalyze;
import com.sofn.bigdata.model.JsonAnalyze;
import com.sofn.bigdata.service.JsonAnalyzeService;
import com.sofn.bigdata.vo.JsonAnalyzeVo;
import com.sofn.bigdata.vo.JsonAnalyzeVo;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JsonAnalyzeServiceImpl  extends BaseService<JsonAnalyzeMapper, JsonAnalyze> implements JsonAnalyzeService {
    
    @Autowired
    private JsonAnalyzeMapper jsonAnalyzeDao;
    @Autowired
    private BusinessSourceMapper businessSourceDao;

    @Override
    public void createJsonAnalyze(JsonAnalyzeVo jsonAnalyzeVo) {
        
        JsonAnalyze jsonAnalyze = new JsonAnalyze();
        BeanUtils.copyProperties(jsonAnalyzeVo,jsonAnalyze);
        jsonAnalyze.setId(IdUtil.getUUId());
        jsonAnalyze.setOperatDate(new Date());
        jsonAnalyze.setOperator(UserUtil.getLoginUserId());
        jsonAnalyze.setCreateTime(new Date());
        jsonAnalyze.setCreateUserId(UserUtil.getLoginUserId());
        jsonAnalyze.setDelFlag("N");

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(jsonAnalyze));
        jsonAnalyzeDao.saveJsonAnalyze(params);

    }

    @Override
    public void deleteJsonAnalyze(String id) {
        jsonAnalyzeDao.deleteJsonAnalyze(id);
    }

    @Override
    public void updateJsonAnalyze(JsonAnalyzeVo jsonAnalyzeVo) {

        JsonAnalyze jsonAnalyze = new JsonAnalyze();
        BeanUtils.copyProperties(jsonAnalyzeVo,jsonAnalyze);
        jsonAnalyze.setOperatDate(new Date());
        jsonAnalyze.setOperator(UserUtil.getLoginUserId());
        jsonAnalyze.setUpdateTime(new Date());
        jsonAnalyze.setUpdateUserId(UserUtil.getLoginUserId());
        jsonAnalyze.setDelFlag("N");

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(jsonAnalyze));
        jsonAnalyzeDao.updateJsonAnalyze(params);
    }

    @Override
    public PageUtils<JsonAnalyzeVo> listPage(Integer pageNo, Integer pageSize, String dataname) {
        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        List<JsonAnalyze> analyzeList = jsonAnalyzeDao.getJsonAnalyzeList(dataname);

        PageInfo<JsonAnalyze> pageInfo = new PageInfo<>(analyzeList);
        List<JsonAnalyze> resList = pageInfo.getList();

        List<JsonAnalyzeVo> resInfo = getResByResList(resList);
        PageInfo<JsonAnalyzeVo> analyzeVoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,analyzeVoPageInfo);
        analyzeVoPageInfo.setList(resInfo);

        return PageUtils.getPageUtils(analyzeVoPageInfo);
    }

    @NotNull
    private List<JsonAnalyzeVo> getResByResList(List<JsonAnalyze> resList) {
        List<JsonAnalyzeVo> voList = new ArrayList<>();
        for(JsonAnalyze source : resList){
            JsonAnalyzeVo vo = new JsonAnalyzeVo();
            BeanUtils.copyProperties(source,vo);
            voList.add(vo);
        }
        return voList;
    }
    
}
