package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

@TableName("DATASHARE")
@Data
public class DataShare extends BaseModel<DataShare> {

    @TableId(type= IdType.UUID)
    private String id;
    private String interId;
    private String interName;
    private String interComment;
    private String interUrl;
    private String interRequest;
    private String interReg;
    private String interParams;
    private String interStatus;
    private String operator;
    private Date operatime;
}
