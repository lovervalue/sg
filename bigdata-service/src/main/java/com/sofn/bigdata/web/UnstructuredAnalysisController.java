package com.sofn.bigdata.web;

import com.sofn.bigdata.model.UnstructuredAnalysis;
import com.sofn.bigdata.service.UnstructuredAnalysisService;
import com.sofn.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Acthor Tao.Lee @date 2021/3/2 17:51
 * @Description 非结构化数据分析
 */
@Slf4j
@Api(value = "非结构化数据分析", tags = "非结构化数据分析")
@RequestMapping("/unstructuredanalysis/")
@RestController
public class UnstructuredAnalysisController {

    @Autowired
    private UnstructuredAnalysisService unstructuredAnalysisService;


    @ApiOperation(value = "新建模型")
    @PostMapping("insert")
    public Result<String> insert(UnstructuredAnalysis unstructuredAnalysis){
        unstructuredAnalysisService.insert(unstructuredAnalysis);
        return Result.ok("插入成功");
    }

    @ApiOperation(value = "查询所有模型")
    @PostMapping("getAllList")
    public List<UnstructuredAnalysis> getAllList(String category, String modelName){
       return unstructuredAnalysisService.getAllList(category, modelName);
    }

    @ApiOperation(value = "查看某一个详情")
    @PostMapping("getOne")
    public UnstructuredAnalysis getOne(String modelName){
        UnstructuredAnalysis unstructuredAnalysis =unstructuredAnalysisService.getOne(modelName);
        return unstructuredAnalysis;
    }

    @ApiOperation(value = "根据模型名称操作并且编辑")
    @PostMapping("updatemodel")
    public void updatemodel( String modelName,Integer status,String keyword,String subordinate,String category,String knowledgeLevel,String fileSource){
        unstructuredAnalysisService.updatemodel(modelName, status, keyword, subordinate, category, knowledgeLevel, fileSource);
    }

    @ApiOperation(value = "智能信息处理-查询文件")
    @PostMapping("getfileList")
    public List getfileList(String fileName, String fileSource,String startDate,String endDate,String category){
        return unstructuredAnalysisService.getfileList(fileName, fileSource, startDate, endDate, category);
    }

    @ApiOperation(value = "智能信息处理-剔除")
    @PostMapping("deleteinformation")
    public void deleteinformation(String modelName,Integer flag){
         unstructuredAnalysisService.deleteinformation(modelName, flag);
    }

    @ApiOperation(value = "智能信息处理-写评估")
    @PostMapping("writeevaluation")
    public void writeevaluation(String modelName,String evaluation){
        unstructuredAnalysisService.writeevaluation(modelName, evaluation);
    }
}





