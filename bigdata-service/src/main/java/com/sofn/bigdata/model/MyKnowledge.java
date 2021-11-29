package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MyKnowledge
 * @Description 模糊搜索知识拼装的返回实体类
 * @Author gouliangcheng
 * @Date 2020/8/3 17:40
 * @Version 1.0
 **/
@Data
@ApiModel("知识点返回实体类")
public class MyKnowledge {

    @TableId(type= IdType.UUID)
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("主知识ID")
    private String knowledgeId;

    @ApiModelProperty("主知识名称")
    private String knowledgeName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("收藏时间")
    private Date createTime;

    @ApiModelProperty("知识点等级1-6")
    private Integer level;
}
