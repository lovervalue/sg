package com.sofn.bigdata.web;

import com.sofn.bigdata.model.MultiDataAnalyze;
import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.service.DataAnalyzeService;
import com.sofn.bigdata.vo.BusinessSourceVo;
import com.sofn.bigdata.vo.DataAnalyzeVo;
import com.sofn.bigdata.vo.DataTreeVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scala.collection.immutable.$colon$colon;

import java.util.Map;

@Slf4j
@Api(tags = "多维数据分析")
@RestController
@RequestMapping("/dataAnalyze")
public class DataAnalyzeController extends BaseController {

    @Autowired
    private DataAnalyzeService dataAnalyzeService;


    @SofnLog("新建数据分析页-新建")
    @ApiOperation(value = "新建数据分析页-新建")
    @PostMapping("/createDataAnalyze")
    public Result<Map<String,Object>> createDataAnalyze(@ApiParam(required = true, value = "业务数据ID") @RequestParam(value = "dataid") String dataid,
                                                               @ApiParam(required = true, value = "数据分析名称") @RequestParam(value = "dataname") String dataname,
                                                               @ApiParam(required = true, value = "描述") @RequestParam(value = "datacomment") String datacomment){
        Map<String,Object> dataTreeVo = dataAnalyzeService.createDataAnalyze(dataid,dataname,datacomment);
        return Result.ok(dataTreeVo);
    }

    @SofnLog("修改数据分析")
    @ApiOperation(value = "修改数据分析")
    @PostMapping("/updateDataAnalyze")
    public Result<Map<String,Object>> updateDataAnalyze(@ApiParam(required = true, value = "数据分析ID") @RequestParam(value = "id") String id){
        try {
            Map<String,Object> dataTreeVo = dataAnalyzeService.updateDataAnalyze(id);
            return  Result.ok(dataTreeVo);
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败：" + e.getMessage());
        }
    }

    @SofnLog("删除数据分析")
    @ApiOperation(value = "删除数据分析", notes = "删除数据分析")
    @DeleteMapping("/deleteDataAnalyze")
    public Result<String> deleteDataAnalyze(@PathVariable String id){
        dataAnalyzeService.deleteDataAnalyze(id);
        return Result.ok("删除成功");
    }

    @SofnLog("分页查询")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/listPage")
    public Result<PageUtils<DataAnalyzeVo>> listPage(@RequestParam(name="pageNo") @ApiParam(value = "从第几条开始",required = true) Integer pageNo,
                                   @RequestParam(name="pageSize") @ApiParam(value = "当前页显示多少条",required = true) Integer pageSize,
                           @ApiParam(name = "dataname", value = "数据分析名称")@RequestParam(value = "dataname", required = false)String dataname){
        PageUtils<DataAnalyzeVo> pageUtils =  dataAnalyzeService.listPage(pageNo,pageSize,dataname);
        return Result.ok(pageUtils);
    }

    @SofnLog("数据分析详情")
    @ApiOperation(value = "数据分析详情", notes = "数据分析详情")
    @GetMapping("/view")
    public Result<DataAnalyzeVo> view(@PathVariable String id){
        DataAnalyzeVo dataAnalyzeVo = dataAnalyzeService.view(id);
        Result<DataAnalyzeVo> dataAnalyzeVoResult = new Result<>();
        dataAnalyzeVoResult.setMsg("获取成功");
        dataAnalyzeVoResult.setData(dataAnalyzeVo);
        dataAnalyzeVoResult.setCode(200);
        return dataAnalyzeVoResult;
    }
}
