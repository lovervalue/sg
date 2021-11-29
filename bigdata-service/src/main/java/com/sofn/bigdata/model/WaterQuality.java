package com.sofn.bigdata.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 袁彬峰
 * @since 2020-05-21
 */
@Data
@TableName("tb_water_quality")
@ApiModel(value="WaterQuality对象", description="")
public class WaterQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "断面名称	")
    private String siteName;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "测量时间	")
    private LocalDateTime dateTime;

    @ApiModelProperty(value = "pH")
    @TableField("pH")
    private Double pH;

    @ApiModelProperty(value = "溶解氧")
    @TableField("DO")
    private Double DO;

    @ApiModelProperty(value = "氨氮	")
    @TableField("NH4")
    private Double NH4;

    @ApiModelProperty(value = "高锰酸盐指数	")
    @TableField("CODMn")
    private Double CODMn;

    @ApiModelProperty(value = "总有机碳	")
    @TableField("TOC")
    private Double TOC;

    @ApiModelProperty(value = "水质类别	")
    private String level;

    @ApiModelProperty(value = "断面属性	")
    private String attribute;

    @ApiModelProperty(value = "站点情况")
    private String status;

    @ApiModelProperty(value = "爬取日期")
    private Date crawlingDate;

    @ApiModelProperty(value = "爬取时间点")
    private Date crawlingTime;

    @ApiModelProperty(value = "模板id")
    private Long templateId;
}
