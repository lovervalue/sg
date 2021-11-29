package com.sofn.bigdata.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

@TableName("BUSI_SOURCE")
@Data
public class BusinessSource extends BaseModel<BusinessSource> {

    @TableId(type= IdType.UUID)
    private String id;
    private String sourceid;
    private String busiName;
    private String busiAlias;
    private String busiComment;
    private String dataJson;
    private String operator;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operatDate;
}
