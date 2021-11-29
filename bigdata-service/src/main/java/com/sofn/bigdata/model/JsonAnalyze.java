package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

@TableName("JSON_ANALYZE")
@Data
public class JsonAnalyze extends BaseModel<JsonAnalyze> {

    @TableId(type= IdType.UUID)
    private String id;
    private String fileName;
    private String fileAlias;
    private String fileSource;
    private String operator;
    private Date operatDate;
    
}
