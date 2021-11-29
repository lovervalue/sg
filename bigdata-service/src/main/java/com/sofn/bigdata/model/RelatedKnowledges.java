package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author gouliangcheng
 * @Description 知识网络返回类
 * @Date 14:08 2020/9/17
 * @Param
 * @return
 **/
@Data
@ApiModel("知识网络返回类")
public class RelatedKnowledges {

    @TableId(type= IdType.UUID)
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("主知识id")
    private String knowledgeId;

    @ApiModelProperty("主知识名称")
    private String knowledgeName;

    @ApiModelProperty("关联知识id")
    private String relatedknowledgeId;

    @ApiModelProperty("关联知识名称")
    private String relatedknowledgeName;

    @ApiModelProperty("主知识作者")
    private String author;

    @ApiModelProperty("主知识摘要")
    private String abstracts;

    @ApiModelProperty("关联知识作者")
    private String relatedAuthor;

    @ApiModelProperty("关联知识摘要")
    private String relatedAbstracts;

    @ApiModelProperty("两知识点的关系    S 上下级,P 平级,L 下上级")
    private String associatedFlag;
}
