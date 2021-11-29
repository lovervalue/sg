package com.sofn.bigdata.sysapi.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家重点管理外来入侵物种分布图VO
 *
 * @author jiangtao
 * @version 1.0
 **/
@Data
@ApiModel
public class AgzimlKmAlienSpeciesDistributionVo implements Serializable {
    private static final long serialVersionUID = 5999717574142646415L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", position = 0)
    @JSONField(ordinal = 0)
    private String id;
    /**
     * 生物类型
     */
    @ApiModelProperty(value = "生物类型(1:植物 2:动物 3:微生物)", position = 1)
    @JSONField(ordinal = 1)
    private String biotype;
    /**
     * 中文名称
     */
    @ApiModelProperty(value = "中文名称", position = 2)
    @JSONField(ordinal = 2)
    private String chineseName;
    /**
     * 拉丁学名
     */
    @ApiModelProperty(value = "拉丁学名", position = 3)
    @JSONField(ordinal = 3)
    private String latinName;

    /**
     * 国内分布(逗号分隔)
     */
    @ApiModelProperty(value = "国内分布(逗号分隔)", position = 4)
    @JSONField(ordinal = 4)
    private String domesticDistribute;
    /**
     * 国内分布的区划编码(逗号分隔)
     */
    @ApiModelProperty(value = "国内分布的区划编码(逗号分隔)", position = 5)
    @JSONField(ordinal = 5)
    private String domesticDistributeCode;
    /**
     * 国外分布(逗号分隔)
     */
    @ApiModelProperty(value = "国外分布(逗号分隔)", position = 6)
    @JSONField(ordinal = 6)
    private String overseasDistribute;
}
