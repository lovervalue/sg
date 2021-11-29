package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RelatedKnowledgeVo
 * @Description 关联知识点专业
 * @Author gouliangcheng
 * @Date 2020/8/4 16:32
 * @Version 1.0
 **/
@Data
public class RelatedKnowledgeVo {

    @ApiModelProperty("主知识id")
    private String knowledgeId;

    @ApiModelProperty("关联知识id")
    private String relatedknowledgeId;

    @ApiModelProperty("两知识点的关系（新增关联知识时不需要填此项）  S 上下级,P 平级,L 下上级")
    private String associatedFlag;
}
