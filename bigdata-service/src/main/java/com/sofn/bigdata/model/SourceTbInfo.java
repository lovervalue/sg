package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

@TableName("SOURCE_TB_INFO")
@Data
public class SourceTbInfo extends BaseModel<SourceTbInfo> {

    @TableId(type= IdType.UUID)
    private String id;
    private String sourceid;
    private String tbName;
    private String tbAlias;
    private String tbStatus;

}
