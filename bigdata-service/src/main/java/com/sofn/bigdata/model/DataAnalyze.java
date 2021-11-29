package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

import java.util.Date;

@TableName("DATA_ANALYZE")
@Data
public class DataAnalyze extends BaseModel<DataAnalyze> {

    @TableId(type= IdType.UUID)
    private String id;
    private String analyzeName;
    private String busiId;
    private String dataComment;
    private String operator;
    private Date operatDate;
    private String gramType;
    private String dataDetail;

}
