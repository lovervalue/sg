package com.sofn.bigdata.sysapi;

import com.sofn.bigdata.vo.SysOrgVo;
import com.sofn.bigdata.vo.SysOrganizationForm;
import com.sofn.bigdata.vo.SysRegionInfoVo;
import com.sofn.common.config.FeignConfiguration;
import com.sofn.common.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(
        value = "sys-service",
        configuration = FeignConfiguration.class
)
public interface SysOrgApi {

    @GetMapping("/sysOrganization/getSysRegionInfoByOrgId/{orgId}")
    Result<SysRegionInfoVo> getSysRegionInfoByOrgId(@PathVariable("orgId") String orgId);

    @GetMapping("/sysOrganization/listTree")
    Result<List<SysOrgVo>> listTree(@RequestParam(name = "appId", required = false) String appId,
                                    @RequestParam(name = "orgName", required = false) String orgName,
                                    @RequestParam(name = "regionCode", required = false) String regionCode,
                                    @RequestParam(name = "orgLevel", required = false) String orgLevel,
                                    @RequestParam(name = "isChildren", required = false) String isChildren,
                                    @RequestParam(name = "isAuth", required = false) String isAuth,
                                    @RequestParam(name = "thirdOrg", required = false) String thirdOrg
    );

    @GetMapping("/sysOrganization/getAllSysOgrInfo")
    Result<List<SysOrgVo>> getAllByAppId(@RequestParam(name = "appId") String appId,
                                         @RequestParam(name = "orgname", required = false) String orgname,
                                         @RequestParam(name = "regionid", required = false) String regionid);

    @GetMapping("/sysOrganization/getAllOrgByLoginUser")
    Result<List<SysOrgVo>> getAllOrgByLoginUser(@RequestParam(value = "appId", required = false) String appId);

    @PostMapping("/sysOrganization/getOrgInfoById")
    Result<SysOrganizationForm> getOrgInfoById(@NotNull @RequestParam("id") String id);
}
