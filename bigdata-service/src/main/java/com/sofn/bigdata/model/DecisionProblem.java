package com.sofn.bigdata.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("tb_decision_problem")
@ApiModel(value="决策问题实体类")
public class DecisionProblem {

    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    @ApiModelProperty("问题名称")
    private String name;
    @ApiModelProperty("问题描述")
    private String problemDescription;
    @ApiModelProperty("问题分类")
    private String problemType;
    @ApiModelProperty("问题级别")
    private String problemLevel;
    @ApiModelProperty("问题性质")
    private String problemNature;
    @ApiModelProperty("问题从属关系")
    private String problemAffiliation;
    @ApiModelProperty("关联问题ids：多个问题id以英文逗号隔开")
    private String relatedProblemIds;
    @ApiModelProperty("关联问题名字:多个问题名以英文逗号隔开")
    private String relatedProblemNames;
    @ApiModelProperty("决策创建人")
    private String creator;
    @ApiModelProperty("决策创建人Id")
    private String creatorId;
    @ApiModelProperty("操作时间")
    private Date createTime;
    @ApiModelProperty("状态:1-提问中；2-提案进行中；3-完成")
    private String status;
    @ApiModelProperty("是否有问题关联或从属:Y或N")
    private String isRelated;

    @ApiModelProperty("列表:提案名称")
    @TableField(exist = false)
    private String proposalName;
    @ApiModelProperty("列表:提案人")
    @TableField(exist = false)
    private String proposalNamePerson;
    @ApiModelProperty("详情:选定提案")
    @TableField(exist = false)
    private Proposal proposal;
    @ApiModelProperty("详情:备份提案")
    @TableField(exist = false)
    private List<Proposal> proposals;

}
