package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

@TableName("TABLE_RELATION")
@Data
public class TableRelation extends BaseModel<TableRelation> {

    private String id;
    private String leftTbName;
    private String leftTbColumn;
    private String unionType;
    private String rightTbName;
    private String rightTbColumn;
    private String tbComment;
    private String moduleid;

}
