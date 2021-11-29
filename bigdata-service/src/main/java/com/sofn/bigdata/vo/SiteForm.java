package com.sofn.bigdata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
@ApiModel(value="站点VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class SiteForm {
    @ApiModelProperty("站点ID")
    @Length(max = 32,message = "站点ID超长")
    private String id;


    @ApiModelProperty("站点地址")
    @Length(max = 50,message = "站点地址超长")
    private String  address;

    @ApiModelProperty("站点地址名称")
    private String  addressName;

    @ApiModelProperty("项目类型")
    @Length(max = 32,message = "项目类型超长")
    private String  prjType;

    @NotBlank(message = "站点名称不能为空")
    @ApiModelProperty("站点名称")
    @Length(max = 20,message = "站点名称超长")
    private String  siteName;

    @NotBlank(message = "公司不能为空")
    @ApiModelProperty("公司ID")
    @Length(max = 32,message = "公司ID超长")
    private String  companyId;

    @ApiModelProperty("公司编码")
    private String  companyCode;

    @ApiModelProperty("经度xx.xxxxxx")
    /*@Range(min = 73, max = 136, message = "经度不合适")*/
    private String  longtitude;

    @ApiModelProperty("纬度xx.xxxxxx")
    /*@Range(min = 3, max = 54, message = "纬度不合适")*/
    private String  latitude;

    @ApiModelProperty("状态")
    @Length(max = 4,message = "状态超长")
    private String  status;

    @NotBlank(message = "站点编码不能为空")
    @ApiModelProperty("站点编码")
    @Length(max = 30,message = "站点编码超长")
    private String  code;

    @ApiModelProperty("备注")
    @Length(max = 255,message = "备注超长")
    private String  remark;

    @ApiModelProperty("负责人邮箱")
    @Length(max = 20,message = "负责人邮箱超长")
    private String  leaderEmail;

    @ApiModelProperty("负责人电话")
    @Length(max = 20,message = "负责人电话超长")
    private String  leaderPhone;

    @ApiModelProperty("负责人")
    @Length(max = 32,message = "负责人超长")
    private String  leader;
    // 创建人
    @ApiModelProperty("创建人ID")
    private String createUserId;
    // 创建时间
    @ApiModelProperty("创建时间")
    private Date createTime;
    // 更新人
    @ApiModelProperty("修改人ID")
    private String updateUserId;
    // 更新时间
    @ApiModelProperty("修改时间")
    private Date updateTime;
    public SiteForm(AstcapSite site) {
        this.id = site.getId();
        this.address = site.getAddress();
        this.siteName = site.getSiteName();
        this.status = site.getStatus();
        this.code = site.getCode();
        this.remark = site.getRemark();
        this.leader = site.getLeader();
        this.leaderEmail = site.getLeaderEmail();
        this.leaderPhone = site.getLeaderPhone();
        this.latitude = site.getLatitude();
        this.longtitude = site.getLongtitude();
        this.companyId = site.getCompanyId();

    }
    public static AstcapSite getSite(SiteForm siteForm){
        AstcapSite site = new AstcapSite();
        BeanUtils.copyProperties(siteForm,site);
        return site;
    }

}
