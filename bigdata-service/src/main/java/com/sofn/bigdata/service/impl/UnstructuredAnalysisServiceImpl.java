package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sofn.bigdata.mapper.KnowledgeMapper;
import com.sofn.bigdata.mapper.UnstructuredAnalysisMapper;
import com.sofn.bigdata.model.Knowledge;
import com.sofn.bigdata.model.UnstructuredAnalysis;
import com.sofn.bigdata.service.UnstructuredAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/3/2 18:31
 * @Description
 */
@Service
public class UnstructuredAnalysisServiceImpl implements UnstructuredAnalysisService {

    @Autowired
    private UnstructuredAnalysisMapper unstructuredAnalysisDao;

    @Autowired
    private KnowledgeMapper knowledgeDao;

    @Override
    public void insert(UnstructuredAnalysis unstructuredAnalysis) {
        unstructuredAnalysis.setCreateTime(new Date());
        unstructuredAnalysisDao.insert(unstructuredAnalysis);
    }

    @Override
    public List<UnstructuredAnalysis> getAllList(String category, String modelName) {
        return unstructuredAnalysisDao.selectList(
                new QueryWrapper<UnstructuredAnalysis>()
                        .eq("category",category)
                        .eq("model_name",modelName)
        );
    }

    @Override
    public UnstructuredAnalysis getOne(String modelName) {
        UpdateWrapper<UnstructuredAnalysis> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("model_name",modelName)
                .set("see_status", 1);
        unstructuredAnalysisDao.update(null, updateWrapper);
        return unstructuredAnalysisDao.selectOne(new QueryWrapper<UnstructuredAnalysis>()
                .eq("model_name",modelName)
        );
    }

    @Override
    public void updatemodel(String modelName, Integer status, String keyword, String subordinate, String category, String knowledgeLevel, String fileSource) {
        UpdateWrapper<UnstructuredAnalysis> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("model_name",modelName)
                .set("status", status)
                .set("keyword",keyword)
                .set("category",category)
                .set("subordinate",subordinate)
                .set("knowledge_level",knowledgeLevel)
                .set("file_source",fileSource);
        unstructuredAnalysisDao.update(null, updateWrapper);
    }

    @Override
    public List getfileList(String fileName, String fileSource, String startDate, String endDate, String category) {
        List list=new ArrayList();
        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stopdaten=startDate+" 00:00:00";
        String startdaten=endDate+" 00:00:00";
        Date startdates=null;//sdf.parse(startdate);
        Date stopdatestf=null;
        try{
            stopdatestf=sdf.parse(stopdaten);
            startdates=sdf.parse(startdaten);
        }catch(ParseException e) {
            e.printStackTrace();
        }
        System.out.println(startdates+"=====" +stopdatestf+"====="+fileSource);
        if( fileSource.equals("业务系统") ) {
            System.out.println("正确");
            list = unstructuredAnalysisDao.selectList(
                    new QueryWrapper<UnstructuredAnalysis>()
                            .eq("category",category)
                            .eq("flag",1)
                            .eq("file_source","业务系统")
                            .lt("create_time",startdates)
                            .ge("create_time",stopdatestf)
            );
        }
        return list;
    }

    @Override
    public void deleteinformation(String modelName, Integer flag) {
        UpdateWrapper<UnstructuredAnalysis> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("model_name",modelName)
                .set("flag", 1);
        unstructuredAnalysisDao.update(null, updateWrapper);
    }


    @Override
    public void writeevaluation(String modelName, String evaluation) {
        UpdateWrapper<UnstructuredAnalysis> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("model_name",modelName)
                .set("evaluation", evaluation);
        unstructuredAnalysisDao.update(null, updateWrapper);
    }


}
