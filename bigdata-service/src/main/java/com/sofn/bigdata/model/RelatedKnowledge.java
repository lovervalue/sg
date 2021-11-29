package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RelatedKnowledge
 * @Description 知识网络返回实体类
 * @Author gouliangcheng
 * @Date 2020/8/4 17:18
 * @Version 1.0
 **/
@Data
@ApiModel("知识网络返回实体类")
public class RelatedKnowledge {

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

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("摘要")
    private String abstracts;

    @ApiModelProperty("两知识点的关系    S 上下级,P 平级,L 下上级")
    private String associatedFlag;

    @ApiModelProperty("等级")
    private Integer level;
}
