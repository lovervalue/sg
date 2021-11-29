package com.sofn.bigdata.web;

import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.service.JsonAnalyzeService;
import com.sofn.bigdata.service.JsonAnalyzeService;
import com.sofn.bigdata.vo.BusiDataVo;
import com.sofn.bigdata.vo.JsonAnalyzeVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Api(tags = "数据分析-文本挖掘")
@RestController
@RequestMapping("/jsonAnalyze")
public class JsonAnalyzeController extends BaseController {
    
    @Autowired
    private JsonAnalyzeService jsonAnalyzeService;


    @SofnLog("文本挖掘-保存")
    @ApiOperation(value = "文本挖掘-保存")
    @PostMapping("/createJsonAnalyze")
    public Result<String> createJsonAnalyze(@Validated @ApiParam(name = "保存数据分类信息", value = "传入json格式", required = true) @RequestBody JsonAnalyzeVo jsonAnalyzeVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            jsonAnalyzeService.createJsonAnalyze(jsonAnalyzeVo);
            return Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("修改文本挖掘")
    @ApiOperation(value = "修改文本挖掘")
    @PostMapping("/updateJsonAnalyze")
    public Result<String> updateJsonAnalyze(@Validated @ApiParam(name = "保存数据分类信息", value = "传入json格式", required = true) @RequestBody JsonAnalyzeVo jsonAnalyzeVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            jsonAnalyzeService.updateJsonAnalyze(jsonAnalyzeVo);
            return  Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败：" + e.getMessage());
        }
    }

    @SofnLog("删除文本挖掘")
    @ApiOperation(value = "删除文本挖掘", notes = "删除文本挖掘")
    @DeleteMapping("/deleteJsonAnalyze")
    public Result<String> deleteJsonAnalyze(@PathVariable String id){
        jsonAnalyzeService.deleteJsonAnalyze(id);
        return Result.ok("删除成功");
    }

    @SofnLog("分页查询")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/listPage")
    public Result<PageUtils<JsonAnalyzeVo>> listPage(@RequestParam(name="pageNo") @ApiParam(value = "从第几条开始",required = true) Integer pageNo,
                                                     @RequestParam(name="pageSize") @ApiParam(value = "当前页显示多少条",required = true) Integer pageSize,
                                                     @ApiParam(name = "dataname", value = "数据分析名称")@RequestParam(value = "dataname", required = false)String dataname){
        PageUtils<JsonAnalyzeVo> pageUtils =  jsonAnalyzeService.listPage(pageNo,pageSize,dataname);
        return Result.ok(pageUtils);
    }
    
}
