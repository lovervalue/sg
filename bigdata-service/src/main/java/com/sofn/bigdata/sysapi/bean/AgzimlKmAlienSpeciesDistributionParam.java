package com.sofn.bigdata.sysapi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家重点管理外来入侵物种分布图参数类
 *
 * @author jiangtao
 * @version 1.0
 **/
@ApiModel
@Data
public class AgzimlKmAlienSpeciesDistributionParam implements Serializable {
    private static final long serialVersionUID = 7757753018271156466L;
    /**
     * 模糊查询(中文名称/拉丁学名)
     */
    @ApiModelProperty(value = "模糊查询(中文名称)")
    private String searchStr;

    /**
     * 生物类型(1:植物 2:动物 3:微生物)
     */
    @ApiModelProperty(value = "生物类型(1:植物 2:动物 3:微生物)", allowableValues = "1,2,3")
    private String biotype;
}
