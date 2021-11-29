package com.sofn.bigdata.web;

import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.model.DataAnalyze;
import com.sofn.bigdata.model.Proposal;
import com.sofn.bigdata.service.ProposalService;
import com.sofn.bigdata.vo.DataAnalyzeVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "决策提案管理接口" )
@RestController
@RequestMapping("/proposal")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @GetMapping("/listPage")
//    @RequiresPermissions("bigdata:proposal:list")
    @ApiOperation(value = "获取所有决策提案列表分页及条件查询")
    public Result<List<Proposal>>  getSurveyTasksPage(@RequestParam(required = false,defaultValue = "1") @ApiParam(value = "当前页数") Integer pageNo,
                                                             @RequestParam(required = false,defaultValue = "10") @ApiParam(value = "每页显示条数") Integer pageSize,
                                                             @RequestParam(required = false) @ApiParam(value = "决策问题名称") String name,
                                                             @RequestParam(required = false) @ApiParam(value = "决策提案状态") String status){
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Proposal> listPage = proposalService.selectListByQuery(pageNo,pageSize,name,status);
        return Result.ok(PageUtils.getPageUtils(listPage));

    }

    @PostMapping("/insert")
//    @RequiresPermissions("bigdata:proposal:insert")
    @ApiOperation(value = "添加/修改决策提案",notes = "第一次为添加，之后则为修改,修改时该提案id必传")
    public Result<Proposal> insertProposal(@RequestBody Proposal proposal) {
        try {
            proposalService.insertProposal(proposal);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
//    @RequiresPermissions("bigdata:proposal:delete")
    @ApiOperation(value = "通过id删除决策提案")
    public Result<Proposal> deleteById(@RequestParam(required = false) String id) {
        try {
            proposalService.remove(id);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    /*@PutMapping("/update")
//    @RequiresPermissions("bigdata:proposal:update")
    @ApiOperation(value = "更新决策提案")
    public Result<Proposal> updateSurveyTasksById(@RequestBody Proposal proposal) {
        try {
            proposalService.update(proposal);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }*/

    @GetMapping("/view")
//    @RequiresPermissions("bigdata:proposal:view")
    @ApiOperation(value = "决策提案详情",notes = "存在多条修改版本,返回最新三条数据：当前,上一版，上上版")
    public Result<List<Proposal>> report(@RequestParam(required = false) String id) {
        try {
            List<Proposal> decisionProbles =proposalService.view(id);
            return Result.ok(decisionProbles,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    @PutMapping("/approver")
//    @RequiresPermissions("bigdata:proposal:approver")
    @ApiOperation(value = "决策提案批准功能")
    public Result approve(@RequestParam(required = false) String id) {
        try {
            proposalService.approver(id);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/annotation")
//    @RequiresPermissions("bigdata:proposal:annotation")
    @ApiOperation(value = "决策追踪:批注添加及修改",notes = "传入批注内容及提案id")
    public Result<Proposal> annotation(@RequestParam(required = false) String annotation,@RequestParam(required = false) String id) {
        try {
            proposalService.insertAnnotation(id,annotation);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }



    @ApiOperation(value = "获取多维分析数据所有数据", notes = "获取多维分析数据所有数据")
    @GetMapping("/getDataAnalyze")
    public List<DataAnalyze> getDataAnalyze(){
        List<DataAnalyze> list =  proposalService.getDataAnalyze();
        return list;
    }

}
