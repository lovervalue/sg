package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@TableName("BUSI_DATA")
@Data
public class BusiData extends BaseModel<BusiData> {

    @TableId(type= IdType.UUID)
    private String id;
    private String sourceid;
    private String pid;
    private String columnType;
    private String dataname;
    private String dataAlias;
    private String dataComment;
    private String dataType;
    private String dataForm;

}
