package com.sofn.bigdata.sysapi.bean;

import com.baomidou.mybatisplus.annotation.TableName;

import com.sofn.common.model.BaseModel;
import lombok.Data;

@TableName("ASTCAP_SITE")
@Data
public class AstcapSite extends BaseModel<AstcapSite> {

    private String  siteName;
    private String  address;
    private String  code;
    private String  latitude;
    private String  longtitude;
    private String  companyId;
    private String  prjType;
    private String  leader;
    private String  status;
    private String  remark;
    private String  leaderEmail;
    private String  leaderPhone;
    public AstcapSite(String siteName) {
        this.siteName = siteName;
    }
    public AstcapSite() {
    }
}

