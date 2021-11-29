package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.mapper.BusinessSourceMapper;
import com.sofn.bigdata.mapper.DataAnalyzeMapper;
import com.sofn.bigdata.model.BusinessSource;
import com.sofn.bigdata.model.DataAnalyze;
import com.sofn.bigdata.service.DataAnalyzeService;
import com.sofn.bigdata.vo.DataAnalyzeVo;
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
public class DataAnalyzeServiceImpl extends BaseService<DataAnalyzeMapper, DataAnalyze> implements DataAnalyzeService {

    @Autowired
    private DataAnalyzeMapper dataAnalyzeDao;
    @Autowired
    private BusinessSourceMapper businessSourceDao;

    @Override
    public Map<String,Object> createDataAnalyze(String dataid, String dataname, String datacomment) {

        DataAnalyze dataAnalyze = new DataAnalyze();
        dataAnalyze.setAnalyzeName(dataname);
        dataAnalyze.setBusiId(dataid);
        dataAnalyze.setDataComment(datacomment);
        dataAnalyze.setId(IdUtil.getUUId());
        dataAnalyze.setOperatDate(new Date());
        dataAnalyze.setOperator(UserUtil.getLoginUserId());
        dataAnalyze.setCreateTime(new Date());
        dataAnalyze.setCreateUserId(UserUtil.getLoginUserId());
        dataAnalyze.setDelFlag("N");

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(dataAnalyze));
        dataAnalyzeDao.saveDataAnalyze(params);

        BusinessSource businessSource = businessSourceDao.selectById(dataid);
        /*String sourceid = DataAnalyze.getSourceid();*/
        String json = businessSource.getDataJson();

        return JSONObject.parseObject(json);
    }

    @Override
    public void deleteDataAnalyze(String id) {
        dataAnalyzeDao.deleteDataAnalyze(id);
    }

    @Override
    public Map<String,Object> updateDataAnalyze(String id) {
        DataAnalyze dataAnalyze = dataAnalyzeDao.selectById(id);
        String busiId = dataAnalyze.getBusiId();
        BusinessSource businessSource = businessSourceDao.selectById(busiId);
        String json = businessSource.getDataJson();
        return JSONObject.parseObject(json);
    }

    @Override
    public PageUtils<DataAnalyzeVo> listPage(Integer pageNo, Integer pageSize, String dataname) {
        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        List<DataAnalyze> analyzeList = dataAnalyzeDao.getDataAnalyzeList(dataname);

        PageInfo<DataAnalyze> pageInfo = new PageInfo<>(analyzeList);
        List<DataAnalyze> resList = pageInfo.getList();

        List<DataAnalyzeVo> resInfo = getResByResList(resList);
        PageInfo<DataAnalyzeVo> analyzeVoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,analyzeVoPageInfo);
        analyzeVoPageInfo.setList(resInfo);

        return PageUtils.getPageUtils(analyzeVoPageInfo);
    }

    @Override
    public DataAnalyzeVo view(String id) {
        DataAnalyze view = dataAnalyzeDao.view(id);
        DataAnalyzeVo vo = new DataAnalyzeVo();
        BeanUtils.copyProperties(view,vo);
        return vo;
    }

    @NotNull
    private List<DataAnalyzeVo> getResByResList(List<DataAnalyze> resList) {
        List<DataAnalyzeVo> voList = new ArrayList<>();
        for(DataAnalyze source : resList){
            DataAnalyzeVo vo = new DataAnalyzeVo();
            BeanUtils.copyProperties(source,vo);
            voList.add(vo);
        }
        return voList;
    }
    
}
