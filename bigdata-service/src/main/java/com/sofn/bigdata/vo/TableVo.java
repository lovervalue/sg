package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@ApiModel(value="数据表VO对象")
public class TableVo {

    @ApiModelProperty(value = "ID")
    @Length(max = 32,message = "ID超长")
    private String id;

    @ApiModelProperty(value = "表名称")
    private String tableName;

    @ApiModelProperty(value = "表注释")
    private String tableComment;

    @ApiModelProperty(value = "字段集合")
    private List<ColumnVo> columnVos;

}
