package com.sofn.bigdata.web;

import com.sofn.bigdata.service.BusiDataService;
import com.sofn.bigdata.vo.BusiDataVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据管理-业务数据-数据分类
 */
@Slf4j
@Api(tags = "数据管理-业务数据-数据分类")
@RestController
@RequestMapping("/busiData")
public class BusiDataController extends BaseController {

    @Autowired
    private BusiDataService busiDataService;

    @SofnLog("保存数据分类信息")
    @ApiOperation(value = "保存数据分类信息",notes = "权限码：（bigdata:BusiData:saveBusiData）")
//    @RequiresPermissions("bigdata:BusiData:saveBusiData")
    @PostMapping("/saveBusiData")
    public Result<String> saveBusiData(@Validated @ApiParam(name = "保存数据分类信息", value = "传入json格式", required = true) @RequestBody BusiDataVo busiDataVo, BindingResult result){
        if (result.hasErrors()) {
            return Result.error(getErrMsg(result));
        }
        try {
            busiDataService.saveBusiData(busiDataVo);
            return  Result.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("保存失败：" + e.getMessage());
        }
    }

}
