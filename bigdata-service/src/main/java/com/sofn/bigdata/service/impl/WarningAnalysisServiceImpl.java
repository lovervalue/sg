package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.sofn.bigdata.mapper.*;
import com.sofn.bigdata.model.*;
import com.sofn.bigdata.service.WarningAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/7 11:18
 * @Description
 */
@Service
public class WarningAnalysisServiceImpl implements WarningAnalysisService {

    @Autowired
    private WarningAnalysisMapper warningAnalysisDao;

    @Autowired
    private ThresholdmodelMapper thresholdmodelDao;

    @Autowired
    private WarningResultMapper warningResultDao;

    @Autowired
    private WarningDetailsMapper warningDetailsDao;

    @Autowired
    private WarningDisposalMapper warningDisposalDao;


    @Override
    public void insert(WarningAnalysis warningAnalysis) {
        warningAnalysis.setCreateTime(new Date());
        warningAnalysisDao.insert(warningAnalysis);
    }

    @Override
    public WarningAnalysis getOne(String dataName, Integer warningStatus) {
        return warningAnalysisDao.selectOne(
                new QueryWrapper<WarningAnalysis>()
                        .eq("data_name",dataName)
                        .eq("warning_status",warningStatus));

    }

    @Override
    public List<WarningAnalysis> getList() {
        return warningAnalysisDao.selectList(
                new QueryWrapper<WarningAnalysis>());
    }

    @Override
    public void insertOthername(String databaseName, String dataName, String businessOthername) {
        WarningAnalysis warningAnalysis=new WarningAnalysis();
        warningAnalysis.setCreateTime(new Date());
        warningAnalysis.setBusinessOthername(businessOthername);
        warningAnalysis.setDataName(dataName);
        warningAnalysis.setDatabaseName(databaseName);
        warningAnalysisDao.insert(warningAnalysis);
    }

    @Override
    public WarningAnalysis getOthername(String databaseName) {
        return warningAnalysisDao.selectOne(
                new QueryWrapper<WarningAnalysis>()
                        .eq("database_name",databaseName));
    }

    @Override
    public void insertThresholdmodel(Thresholdmodel thresholdmodel) {

        thresholdmodel.setCreateTime(new Date());
        thresholdmodelDao.insert(thresholdmodel);

    }

    @Override
    public void deleteThresholdmodel(Integer thresholdvalue) {
        Thresholdmodel thone=thresholdmodelDao.selectOne(new QueryWrapper<Thresholdmodel>()
                .eq("yield",thresholdvalue));
        if(null!=thone){
            thresholdmodelDao.delete(new QueryWrapper<Thresholdmodel>()
                    .eq("yield",thresholdvalue));
        }
        Thresholdmodel thtwo=thresholdmodelDao.selectOne(new QueryWrapper<Thresholdmodel>()
                .eq("collectable_amount",thresholdvalue));
        if(null!=thtwo) {
            thresholdmodelDao.delete(new QueryWrapper<Thresholdmodel>()
                    .eq("collectable_amount", thresholdvalue));
        }


    }

    @Override
    public List<WarningResult> getWarningResultList(String dataName, Integer status, String startdate, String stopdate) {
        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startdates=null;//sdf.parse(startdate);
        Date stopdatestf=null;
        try{
            stopdatestf=sdf.parse(stopdate);
            startdates=sdf.parse(startdate);
        }catch(ParseException e) {
            e.printStackTrace();
        }
        System.out.println(startdates+"===="+stopdatestf);
        return warningResultDao.selectList(
                new QueryWrapper<WarningResult>()
                        .eq("data_name",dataName)
                        .eq("status",status)
                        .ge("warning_time",startdates)
                       .le("warning_time",stopdatestf)
        );
    }

    @Override
    public List<WarningDetails> getWarningDetailsList(String dataName) {
        return warningDetailsDao.selectList(
                new QueryWrapper<WarningDetails>().eq("data_name",dataName)
        );
    }

    @Override
    public WarningDisposal getWarningDisposalOne(String dataName) {
        return warningDisposalDao.selectOne(
                new QueryWrapper<WarningDisposal>().eq("data_name",dataName)
        );
    }

    @Override
    public List<WarningResult> getWarningResultoperating(String dataName) {
        return warningResultDao.selectList(
                new QueryWrapper<WarningResult>().eq("data_name",dataName)
        );
    }

    @Override
    public List getWarningAnalysisDisplay(String dataName, String startdate, String stopdate) {
        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startdates=null;//sdf.parse(startdate);
        Date stopdatestf=null;
        try{
            stopdatestf=sdf.parse(stopdate);
            startdates=sdf.parse(startdate);
        }catch(ParseException e) {
            e.printStackTrace();
        }
        return warningResultDao.selectsum(dataName,startdates,stopdatestf);
    }

}
