package com.sofn.bigdata.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@TableName("tb_proposal")
@ApiModel(value="决策提案实体类", description="")
public class Proposal {

  @TableId(value = "id", type = IdType.INPUT)
  private String id;
  @ApiModelProperty("提案名称")
  private String name;
  @ApiModelProperty("提案人")
  private String creator;
  @ApiModelProperty("提案人Id")
  private String creatorId;
  @ApiModelProperty("操作时间")
  private Date createTime;
  @ApiModelProperty("批准人")
  private String approver;
  @ApiModelProperty("状态：1-未入选；2-提案进行中；3-完成")
  private String status;
  @ApiModelProperty("决策问题id")
  private String decisionProblemId;
  @ApiModelProperty("决策问题名称")
  private String decisionProblemName;
  @ApiModelProperty("案由分析")
  private String caseAnalyze;
  @ApiModelProperty("建议措施")
  private String suggestionMeasure;
  @ApiModelProperty("分析数据")
  private String analyzeData;
  @ApiModelProperty("分析数据id")
  private String analyzeDataId;
  @ApiModelProperty("上传文件id")
  private String fileId;
  @ApiModelProperty("上传文件名")
  private String fileName;
  @ApiModelProperty("批注内容")
  private String annotation;
  @ApiModelProperty("批注人")
  private String annotationPerson;
  @ApiModelProperty("批注时间")
  private Date annotationTime;


}
