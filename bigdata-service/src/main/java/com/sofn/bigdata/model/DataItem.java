package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:袁彬锋
 * @date:2020/4/29 10:35
 * @company:曙光集团绿康元公司
 * @description:
 */
@Data
@TableName("tb_data_item")
@ApiModel("数据项实体")
public class DataItem {

    //id
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("id")
    private Long id;

    //数据项名称
    @ApiModelProperty("数据项名称")
    private String name;

    //数据项内容
    @ApiModelProperty("数据项内容")
    private String content;

    //类型
    @ApiModelProperty("类型")
    private String type;

    //格式
    @ApiModelProperty("格式")
    private String format;

    //描述
    @ApiModelProperty("描述")
    private String description;
}
