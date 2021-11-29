package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.vo.*;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import com.sofn.common.utils.shiro.JWTToken;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 使用Feign 调用sys服务
 *
 * @author zhouqingchun
 * @date 2020/05/25 17:05
 */
@FeignClient(
        value = "sys-service",
        configuration = FeignConfiguration.class
)
public interface SysRegionApi {

    /**
     * 获取区县信息
     *
     * @return Result
     */
    @GetMapping(value = "/sysRegion/getSysRegionOne/{areaId}")
    Result<SysRegionForm> getSysRegionOne(@PathVariable(value = "areaId") String areaId);

    /**
     * 获取区县信息
     *
     * @return Result
     */
    @GetMapping(value = "/sysRegion/getSysRegionOne/{areaId}")
    Result<SysRegionForm> getSysRegionOne(@PathVariable(value = "areaId") String areaId, @RequestHeader(name = JWTToken.TOKEN) String token);
    
    /**
     * 根据areaId获取
     *
     * @return Result
     */
    @GetMapping("/sysRegion/getParentNodeByRegionCode/{regionCode}")
    Result<List<SysRegionTreeVo>> getParentNode(@ApiParam(value = "行政区划ID", required = true) @PathVariable(value = "regionCode") String regionCode);

    /**
     * 根据userId获取用户角色
     *
     * @return Result
     */
    @PostMapping("/user/getUserOne")
    Result<SysUserForm> getOne(@ApiParam(name = "id", value = "用户ID", required = true) @RequestParam("id") String id);

    @GetMapping("/sysRegion/getRegionNamesByCodes/{codes}")
    Result<String> getRegionNamesByCodes(@ApiParam(value = "区划代码，多个用逗号分隔") @PathVariable("codes") String codes);

    @GetMapping("/sysRegion/getListByParentId/{parentId}")
    Result<List<SysRegionForm>> getListByParentId(@ApiParam(value = "父节点ID,如果不传入，默认为100000") @PathVariable("parentId") String parentId);

    /**
     * 激活文件
     */
    @PostMapping(value =  "/fileManage/activationFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<SysFileVo>> activationFile(@RequestBody @ApiParam(name = "添加文件对象", value = "传入json格式", required = true)
                                                   SysFileManageForm sysFileManageForm);


    /**
     * 删除支撑平台中的文件
     * @param id：文件id
     * @return
     */
    @PostMapping("/fileManage/deleteFile")
    Result delFile(@ApiParam("要删除的文件ID") @RequestParam("id") String id);

    @GetMapping("/getSysRegionTreeById/{parentId}")
    Result<List<SysRegionTreeVo>> getSysRegionTreeById(@ApiParam(value = "父ID", required = true) @PathVariable("parentId") String parentId,
                                                              @ApiParam(value = "系统APPID") @RequestParam(value = "appId",required = false) String appId);





    @GetMapping(value = "/sysRegion/getSysRegionByName")
    Result<SysRegionForm> getSysRegionByName(@ApiParam(value = "行政区划名称", required = true) @RequestParam(value = "regionName") String regionName);



}
