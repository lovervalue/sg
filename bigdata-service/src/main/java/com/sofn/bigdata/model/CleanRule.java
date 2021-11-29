package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.bigdata.enums.RuleEnum;
import com.sofn.common.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("CLEAN_RULE")
@ApiModel("清洗规则实体")
public class CleanRule extends BaseModel<CleanRule> {

    //id
    @TableId(type= IdType.UUID)
    private String id;

    private String tableId;

    private String fieldName;

    private String fieldType;

    private RuleEnum ruleType;

    private String ruleValue;

    private String delFlag;
}
