package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author:袁彬锋
 * @date:2020/7/29 10:18
 * @company:曙光集团绿康元公司
 * @description:
 */
@Data
@TableName("tb_multi_data_analyze")
@ApiModel("多源数据分析实体类")
public class MultiDataAnalyze {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("数据类型")
    private String dataType;
    @ApiModelProperty("数据分析名称")
    private String dataAnalyzeName;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operationTime;
}
