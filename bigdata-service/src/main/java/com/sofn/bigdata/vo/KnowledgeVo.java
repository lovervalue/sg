package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @ClassName knowledgeVo
 * @Description 传入知识点参数专用
 * @Author gouliangcheng
 * @Date 2020/8/3 15:16
 * @Version 1.0
 **/
@Data
public class KnowledgeVo {

    @ApiModelProperty("主知识名称")
    private String knowledgeName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("摘要")
    private String abstracts;

    @ApiModelProperty("正文")
    private String text;

    @ApiModelProperty("知识点等级1-6")
    private Integer level;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
