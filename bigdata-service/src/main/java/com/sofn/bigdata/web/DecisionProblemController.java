package com.sofn.bigdata.web;

import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.DecisionProblem;
import com.sofn.bigdata.service.DecisionProblemService;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "决策问题管理接口" )
@RestController
@RequestMapping("/decisionProblem")
public class DecisionProblemController {

    @Autowired
    private DecisionProblemService decisionProblemService;

    @GetMapping("/listPage")
//    @RequiresPermissions("bigdata:decisionProblem:list")
    @ApiOperation(value = "获取所有决策问题列表分页及条件查询")
    public Result<List<DecisionProblem>>  getSurveyTasksPage(@RequestParam(required = false,defaultValue = "1") @ApiParam(value = "当前页数") Integer pageNo,
                                                             @RequestParam(required = false,defaultValue = "10") @ApiParam(value = "每页显示条数") Integer pageSize,
                                                             @RequestParam(required = false) @ApiParam(value = "决策问题名称") String name,
                                                             @RequestParam(required = false) @ApiParam(value = "决策问题状态") String status){
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<DecisionProblem> listPage = decisionProblemService.selectListByQuery(pageNo,pageSize,name,status);
        return Result.ok(PageUtils.getPageUtils(listPage));

    }

    @PostMapping("/insert")
//    @RequiresPermissions("bigdata:decisionProblem:insert")
    @ApiOperation(value = "添加决策问题")
    public Result<DecisionProblem> insertDecisionProblem(@RequestBody DecisionProblem decisionProblem) {
        try {
            decisionProblemService.insertDecisionProblem(decisionProblem);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
//    @RequiresPermissions("bigdata:decisionProblem:delete")
    @ApiOperation(value = "通过id删除决策问题")
    public Result<DecisionProblem> deleteById(String id) {
        try {
            decisionProblemService.remove(id);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
//    @RequiresPermissions("bigdata:decisionProblem:update")
    @ApiOperation(value = "更新决策问题")
    public Result<DecisionProblem> updateSurveyTasksById(@RequestBody DecisionProblem decisionProblem) {
        try {
            decisionProblemService.update(decisionProblem);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/view")
//    @RequiresPermissions("bigdata:decisionProblem:view")
    @ApiOperation(value = "决策问题详情")
    public Result<DecisionProblem> report(@RequestParam(required = false) String id) {
        try {
            DecisionProblem decisionProble =decisionProblemService.view(id);
            return Result.ok(decisionProble,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    @PutMapping("/designate")
//    @RequiresPermissions("bigdata:decisionProblem:designate")
    @ApiOperation(value = "决策问题选择提案",notes = "传入该问题id及被选择的提案id")
    public Result designate(@ApiParam(value = "问题id")@RequestParam(required = false) String id,@ApiParam(value = "提案id")@RequestParam(required = false) String proposalId) {
        try {
            decisionProblemService.designate(id,proposalId);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

}
