package com.sofn.bigdata.web;

import com.sofn.bigdata.model.*;
import com.sofn.bigdata.service.WarningAnalysisService;
import com.sofn.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/7 11:20
 * @Description 预警分析接口，全部的都在一块
 */
@Slf4j
@Api(tags = "预警分析接口")
@RestController
@RequestMapping("/warninganalysis/")

public class WarningAnalysisController {

    @Autowired
    private WarningAnalysisService warningAnalysisService;

    @PostMapping("insert")
    @ApiOperation(value = "新建阈值模型")
    public Result<String> insert(WarningAnalysis warningAnalysis){
        warningAnalysisService.insert(warningAnalysis);
        return  Result.ok("添加成功");
    }
    @GetMapping("getOne")
    @ApiOperation(value = "根据业务名称状态查询")
    public Result<String> getOne(String dataName,Integer warningStatus){
        WarningAnalysis object=warningAnalysisService.getOne(dataName,warningStatus);
        return Result.ok(object);
    }

    @GetMapping("getList")
    @ApiOperation(value = "查询全部")
    public List<WarningAnalysis> getList(){
        return warningAnalysisService.getList();
    }

    @GetMapping("insertOthername")
    @ApiOperation(value = "新建别名")
    public void insertOthername(String databaseName,String dataName,String businessOthername){
        warningAnalysisService.insertOthername(databaseName,dataName, businessOthername);
    }

    @GetMapping("getOthername")
    @ApiOperation(value = "查询别名")
    public  WarningAnalysis getOthername(String databaseName){
       return warningAnalysisService.getOthername(databaseName);
    }

    @PostMapping("insertThresholdmodel")
    @ApiOperation(value = "新增阈值模型")
    public Result<String> insertThresholdmodel(Thresholdmodel thresholdmodel){
        warningAnalysisService.insertThresholdmodel(thresholdmodel);
        return  Result.ok("添加成功");
    }
    @PostMapping("deleteThresholdmodel")
    @ApiOperation(value = "删除阈值模型")
    public Result<String> deleteThresholdmodel(Integer thresholdvalue){
        warningAnalysisService.deleteThresholdmodel(thresholdvalue);
        return  Result.ok("删除成功");
    }

    @GetMapping("getWarningResultList")
    @ApiOperation(value = "预警分析结果查询")
    public List<WarningResult> getWarningResultList(String dataName, Integer status, String startdate, String stopdate){
        return warningAnalysisService.getWarningResultList(dataName, status, startdate, stopdate);
    }

    @GetMapping("getWarningDetailsList")
    @ApiOperation(value = "预警分析结果查询详情")
    public List<WarningDetails> getWarningDetailsList(String dataName){
        return warningAnalysisService.getWarningDetailsList(dataName);
    }

    @GetMapping("getWarningDisposalOne")
    @ApiOperation(value = "预警分析结果预警处置")
    public WarningDisposal getWarningDisposalOne(String dataName){
        return warningAnalysisService.getWarningDisposalOne(dataName);
    }

    @GetMapping("getWarningResultoperating")
    @ApiOperation(value = "预警分析结果操作记录")
    public List<WarningResult> getWarningResultoperating (String dataName){
        return warningAnalysisService.getWarningResultoperating(dataName);
    }

    @GetMapping("getWarningAnalysisDisplay")
    @ApiOperation(value = "预警分析展示")
    public List getWarningAnalysisDisplay(String dataName, String startdate, String stopdate){

        return warningAnalysisService.getWarningAnalysisDisplay(dataName, startdate, stopdate);

    }

}














