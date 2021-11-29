package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName Knowledge
 * @Description 知识点返回实体类
 * @Author gouliangcheng
 * @Date 2020/8/4 11:01
 * @Version 1.0
 **/
@Data
@ApiModel("知识点返回实体类")
public class Knowledge {

    @TableId(type = IdType.UUID)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("知识名称")
    private String knowledgeName;

    @ApiModelProperty("知识ID")
    private String knowledgeId;

    @ApiModelProperty("收藏量")
    private Integer collections;

    @ApiModelProperty("当前用户是否收藏: Y N")
    private boolean colFlag;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("摘要")
    private String abstracts;

    @ApiModelProperty("正文")
    private String text;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件地址")
    private String attachmentAddress;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
