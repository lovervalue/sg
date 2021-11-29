package com.sofn.bigdata.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author:袁彬锋
 * @date:2020/4/29 10:01
 * @company:曙光集团绿康元公司
 * @description:模板表实体类
 */
@Data
@TableName("tb_template")
@ApiModel("模板实体")
public class Template implements Serializable {

    //id
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("id")
    private Long id;

    //网络爬取模板名称
    @ApiModelProperty("网络爬取模板名称")
    private String name;

    //执行周期
    @ApiModelProperty("执行周期")
    private String executeCycle;

    //网络地址
    @ApiModelProperty("网络地址")
    private String url;

    //数据项id集合
    @TableField(value = "data_item_ids")
    @ApiModelProperty("数据项id集合")
    private List<Long> dataItemIds;

    //状态是否可用 Y or N
    @ApiModelProperty("状态是否可用 Y or N")
    private String status;

    //操作人
    @ApiModelProperty("操作人")
    private String operator;

    //操作时间
    @ApiModelProperty("保存时间")
    private Date storeDate;

    //更新时间
    @ApiModelProperty("更新时间")
    private Date updateDate;

    //数据项集合
    @TableField(exist = false)
    @ApiModelProperty("数据项集合")
    private List<DataItem> dataItemList;
}
