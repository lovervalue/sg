package com.sofn.bigdata.web;

import com.sofn.bigdata.service.DecisionTemplateIsproService;
import com.sofn.bigdata.vo.BigDataSourceVo;
import com.sofn.bigdata.vo.TemplateIsproVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
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

import java.util.List;

@Slf4j
@Api(tags = "决策问题（或提案）模板管理" )
@RestController
@RequestMapping("/decisionTemplateIspro")
public class DecisionTemplateIsproController extends BaseController {

    @Autowired
    private DecisionTemplateIsproService decisionTemplateIsproService;

    @SofnLog("保存模板字段信息")
    @ApiOperation(value = "保存模板字段信息")
    @PostMapping("/saveTemplateIspro")
    public Result<String> saveTemplateIspro(@Validated @ApiParam(name = "模板字段信息", value = "传入json格式", required = true) @RequestBody TemplateIsproVo templateIsproVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            decisionTemplateIsproService.saveTemplateIspro(templateIsproVo);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("修改模板字段信息")
    @ApiOperation(value = "修改模板字段信息")
    @PutMapping("/updateTemplateIspro")
    public Result<String> updateTemplateIspro(@Validated @ApiParam(name = "模板字段信息", value = "传入json格式", required = true) @RequestBody TemplateIsproVo templateIsproVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            decisionTemplateIsproService.updateTemplateIspro(templateIsproVo);
            return  Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败：" + e.getMessage());
        }
    }

    @SofnLog("查询模板字段信息列表(分页)")
    @ApiOperation(value = "查询模板字段信息列表(分页)")
    @GetMapping("/getTemplateIsproList")
    public Result<PageUtils<TemplateIsproVo>> getTemplateIsproList(@RequestParam(required = false) @ApiParam(value = "字段名称") String fieldname,
                                                                   @RequestParam(required = false) @ApiParam(value = "类型") String fieldtype,
                                                                   @RequestParam(value = "ispro") @ApiParam(required = true, value = "是否问题模板：0问题模板，1提案模板") String ispro,
                                                                   @RequestParam(name="pageNo") @ApiParam(required = true, value = "当前页数") Integer pageNo,
                                                                   @RequestParam(name="pageSize") @ApiParam(required = true, value = "每页显示条数") Integer pageSize){
        PageUtils<TemplateIsproVo> pageUtils = decisionTemplateIsproService.getTemplateIsproList(fieldname,fieldtype,ispro,pageNo,pageSize);
        return Result.ok(pageUtils);
    }

    @SofnLog("查询所有模板字段信息列表(不分页)")
    @ApiOperation(value = "查询所有模板字段信息列表(不分页)")
    @GetMapping("/getAllTemplateIsproList")
    public Result<List<TemplateIsproVo>> getAllTemplateIsproList(@ApiParam(required = true, value = "是否问题模板：0问题模板，1提案模板") @RequestParam(value = "ispro") String ispro){
        List<TemplateIsproVo> templateIsproVoList = decisionTemplateIsproService.getAllTemplateIsproList(ispro);
        return Result.ok(templateIsproVoList);
    }

    @SofnLog("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping("/delTemplateIspro")
    public Result<String> delTemplateIspro(@ApiParam(required = true, value = "模板字段ID") @RequestParam(value = "id") String id){
        try {
            decisionTemplateIsproService.delTemplateIspro(id);
            return  Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("删除失败：" + e.getMessage());
        }
    }

    @SofnLog("启用")
    @ApiOperation(value = "启用")
    @PostMapping("/startTemplateIspro")
    public Result<String> startTemplateIspro(@ApiParam(required = true, value = "模板字段ID") @RequestParam(value = "id") String id){
        try {
            decisionTemplateIsproService.startTemplateIspro(id);
            return  Result.ok("启用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("启用失败：" + e.getMessage());
        }
    }

    @SofnLog("停用")
    @ApiOperation(value = "停用")
    @PostMapping("/stopTemplateIspro")
    public Result<String> stopTemplateIspro(@ApiParam(required = true, value = "模板字段ID") @RequestParam(value = "id") String id){
        try {
            decisionTemplateIsproService.stopTemplateIspro(id);
            return  Result.ok("停用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("停用失败：" + e.getMessage());
        }
    }

}
