package com.sofn.bigdata.web;

import com.sofn.bigdata.service.EnumService;
import com.sofn.bigdata.vo.EnumVo;
import com.sofn.common.log.SofnLog;
import com.sofn.common.model.Result;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "bigdata:枚举类信息")
@RestController
@RequestMapping("/enum")
public class EnumController extends BaseController {
    @Autowired
    private EnumService enumService;

    @SofnLog("查询所有的字段清洗方式")
    @ApiOperation(value = "查询所有的字段清洗方式", notes = "权限码：（bigdata:enum:getRuleEunms）")
    @GetMapping("/getRuleEunms")
    public Result<PageUtils<EnumVo>> getRuleEunms() {
        PageUtils res = enumService.getRuleEunms();
        return Result.ok(res);
    }

    @SofnLog("查询当前code的描述信息")
    @ApiOperation(value = "查询当前code的描述信息", notes = "权限码：（bigdata:enum:getRuleEnumDescBycode）")
    @GetMapping("/getRuleEnumDescBycode")
    public Result<String> getRuleEnumDescBycode(
            @RequestParam(name="code") @ApiParam(value = "状态值",required = true) String code
    ) {
        String res = enumService.getRuleEnumDescBycode(code);
        return Result.ok(res);
    }

}
