package com.sofn.bigdata.web;

import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.constant.StatusCode;
import com.sofn.bigdata.model.PageResult;
import com.sofn.bigdata.model.Template;
import com.sofn.bigdata.service.TemplateService;
import com.sofn.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author:袁彬锋
 * @date:2020/4/29 10:00
 * @company:曙光集团绿康元公司
 * @description:
 */
@Api(tags = "模板管理相关接口", description = "模板添加修改保存等")
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @ApiOperation(value = "模板保存", notes = "模板保存")
    @PostMapping("/insert")
    public Result insert(@RequestBody Template template){
        templateService.insert(template);
        return new Result(StatusCode.OK, Constant.CREATE_TEMPLATE_SUCCESS);
    }

    @ApiOperation(value = "根据id删除模板", notes = "根据id删除模板")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id")Long id){
        templateService.deleteById(id);
        return new Result(StatusCode.OK, Constant.DELETE_TEMPLATE_SUCCESS);
    }

    @ApiOperation(value = "根据id查询模板", notes = "根据id查询模板")
    @GetMapping("/findById/{id}")
    public Result<Template> findById(@PathVariable("id")Long id){
        Template template = templateService.findById(id);
        return new Result(StatusCode.OK, Constant.DELETE_TEMPLATE_SUCCESS, template);
    }

    @ApiOperation(value = "根据id更新模板", notes = "根据id更新模板")
    @PutMapping("/updateById")
    public Result updateById(@RequestBody Template template){
        templateService.updateById(template);
        return new Result(StatusCode.OK, Constant.SAVE_SPIDER_SUCCESS);
    }

    @ApiOperation(value = "多条件分页查询", notes = "多条件分页查询")
    @GetMapping("/pageQuery")
    public PageResult pageQuery(@ApiParam(name = "pageNum", value = "当前页码", required = true)@RequestParam(value = "pageNum", required = true)Long pageNum,
                                @ApiParam(name = "pageSize", value = "每页显示内容数量", required = false)@RequestParam(value = "pageSize", required = false)Long pageSize,
                                @ApiParam(name = "name", value = "网络爬取名称", required = false)@RequestParam(value = "name", required = false)String name,
                                @ApiParam(name = "status", value = "状态", required = false)@RequestParam(value = "status", required = false)String status){
        return templateService.pageQuery(pageNum, pageSize, name, status);
    }
}



