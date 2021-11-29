package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 袁彬峰
 * @since 2020-06-24
 */
@Data
@TableName("tb_tables_info")
@ApiModel(value="表信息", description="")
public class TablesInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表别名")
    private String tableGeneralName;

    @ApiModelProperty(value = "数据来源")
    private String origin;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后一次更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private List<TableStructure> tableStructureList;
}
