package com.sofn.bigdata.web;

import com.sofn.bigdata.service.CleanRuleService;
import com.sofn.bigdata.vo.CleanRuleVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "清洗规则相关接口", description = "清洗规则相关接口")
@RestController
@RequestMapping("/cleanRule")
public class CleanRuleController  extends BaseController {

    @Autowired
    private CleanRuleService cleanRuleService;

    
    @SofnLog("创建")
    @ApiOperation(value = "创建清洗规则",notes = "权限码：（bigdata:cleanRule:save）")
    @PostMapping("/save")
    public Result save(@Validated @ApiParam(name = "清洗规则对象", value = "传入json格式", required = true) @RequestBody CleanRuleVo cleanRuleVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            cleanRuleService.save(cleanRuleVo);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败" +e.getMessage());
        }
    }

    @SofnLog("删除清洗规则")
    @ApiOperation(value = "删除清洗规则",notes = "权限码：（bigdata:cleanRule:delete）")
    @PostMapping("/delete")
    public Result delete(@ApiParam(required = true, value = "清洗规则ID") @RequestParam(value = "id") String id){
        try {
            cleanRuleService.delete(id);
            return  Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("删除失败：" + e.getMessage());
        }
    }

    @SofnLog("修改清洗规则")
    @ApiOperation(value = "修改清洗规则",notes = "权限码：（bigdata:cleanRule:update）")
    @PostMapping("/update")
    public Result<String> update(
            @Validated @ApiParam(name = "清洗规则对象", value = "传入json格式", required = true)@RequestBody CleanRuleVo cleanRuleVo,
                                                     BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            cleanRuleService.update(cleanRuleVo);
            return  Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败" +e.getMessage());
        }
    }

    @SofnLog("查询所有清洗规则")
    @ApiOperation(value = "查询清洗规则",notes = "权限码：（bigdata:cleanRule:getByTableId）")
    @GetMapping("/getByTableId")
    public Result<List> getRuleByTableId(
            @ApiParam(required = true, value = "所关键的爬虫模版ID或共享接口ID") @RequestParam(value = "tableId") String tableId
    ){
        List list = cleanRuleService.getByTableId(tableId);
        return Result.ok(list);
    }
}



