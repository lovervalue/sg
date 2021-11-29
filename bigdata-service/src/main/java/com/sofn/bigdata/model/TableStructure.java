package com.sofn.bigdata.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:袁彬锋
 * @date:2020/6/29 9:58
 * @company:曙光集团绿康元公司
 * @description:
 */
@Data
@ApiModel(value="表结构实体", description="")
public class TableStructure {
    @ApiModelProperty(value = "字段名称")
    private String COLUMN_NAME;

    @ApiModelProperty(value = "字段别名")
    private String COLUMN_COMMENT;

    @ApiModelProperty(value = "字段类型")
    private String DATA_TYPE;
}
