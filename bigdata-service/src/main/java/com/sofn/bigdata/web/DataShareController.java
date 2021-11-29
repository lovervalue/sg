package com.sofn.bigdata.web;

import com.sofn.bigdata.service.DataShareService;
import com.sofn.bigdata.vo.DataShareVo;
import com.sofn.common.exception.SofnException;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
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

/**
 * zhouqingchun
 * 2020-05-25 15:00:00
 */

@Slf4j
@Api(tags = "数据共享")
@RestController
@RequestMapping("/dataShare")
public class DataShareController extends BaseController {

    @Autowired
    private DataShareService dataShareService;

    @SofnLog("创建数据共享")
    @ApiOperation(value = "创建数据共享",notes = "权限码：（bigdata:dataShare:saveDataShare）")
//    @RequiresPermissions("bigdata:dataShare:saveDataShare")
    @PostMapping("/saveDataShare")
    public Result saveDataShare(@Validated @ApiParam(name = "数据共享管理对象", value = "传入json格式", required = true) @RequestBody DataShareVo dataShare, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            dataShareService.saveDataShare(dataShare);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

    @SofnLog("删除数据共享")
    @ApiOperation(value = "删除数据共享",notes = "权限码：（bigdata:dataShare:deleteDataShare）")
//    @RequiresPermissions("bigdata:dataShare:deleteDataShare")
    @DeleteMapping("/deleteDataShare")
    public Result deleteDataShare(@ApiParam(required = true, value = "数据共享管理ID") @RequestParam(value = "id") String id){
        try {
            dataShareService.deleteDataShare(id);
            return  Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("删除失败：" + e.getMessage());
        }
    }

    @SofnLog("修改数据共享")
    @ApiOperation(value = "修改数据共享",notes = "权限码：（bigdata:dataShare:updateDataShare）")
//    @RequiresPermissions("bigdata:dataShare:updateDataShare")
    @PutMapping("/updateDataShare")
    public Result updateDataShare(@Validated @ApiParam(name = "数据共享管理对象", value = "传入json格式", required = true)@RequestBody DataShareVo dataShareVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            dataShareService.updateDataShare(dataShareVo);
            return  Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("修改失败：" + e.getMessage());
        }
    }

    @SofnLog("查询数据共享")
    @ApiOperation(value = "查询数据共享",notes = "权限码：（bigdata:dataShare:getDataShareList）")
//    @RequiresPermissions("bigdata:dataShare:getDataShareList")
    @GetMapping("/getDataShareList")
    public Result<PageUtils<DataShareVo>> getDataShareList(@RequestParam(name="interName",required = false) @ApiParam(value = "接口名称") String interName,
                                                           @RequestParam(name="interStatus",required = false) @ApiParam(value = "状态") String interStatus,
                                                                       @RequestParam(name="pageNo") @ApiParam(value = "从第几条开始",required = true) Integer pageNo,
                                                                       @RequestParam(name="pageSize") @ApiParam(value = "当前页显示多少条",required = true) Integer pageSize){
        /*List<String> roleCodeList = UserUtil.getLoginUserRoleCodeList();
        if(roleCodeList==null||roleCodeList.size()==0){
            throw new SofnException("当前登录用户异常");
        }*/
        PageUtils  res = dataShareService.getDataShareList(interName,interStatus,pageNo,pageSize);
        return Result.ok(res);
    }

    @SofnLog("启动数据共享")
    @ApiOperation(value = "启动数据共享",notes = "权限码：（bigdata:dataShare:startDataShare）")
//    @RequiresPermissions("bigdata:dataShare:startDataShare")
    @PostMapping("/startDataShare")
    public Result startDataShare(@ApiParam(required = true, value = "数据共享管理ID") @RequestParam(value = "id") String id){
        try {
            dataShareService.startDataShare(id);
            return  Result.ok("启动成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("启动失败：" + e.getMessage());
        }
    }

    @SofnLog("禁止数据共享")
    @ApiOperation(value = "禁止数据共享",notes = "权限码：（bigdata:dataShare:notStartDataShare）")
//    @RequiresPermissions("bigdata:dataShare:notStartDataShare")
    @PostMapping("/notStartDataShare")
    public Result notStartDataShare(@ApiParam(required = true, value = "数据共享管理ID") @RequestParam(value = "id") String id){
        try {
            dataShareService.notStartDataShare(id);
            return  Result.ok("禁止成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("禁止失败：" + e.getMessage());
        }
    }

}
