package com.sofn.bigdata.vo;

import com.sofn.bigdata.enums.RuleEnum;
import com.sofn.bigdata.model.CleanRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel(value="清洗规则VO对象")
@NoArgsConstructor
@AllArgsConstructor
public class CleanRuleVo{

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("目标表ID")
    private String tableId;

    @ApiModelProperty("目标字段名")
    private String fieldName;

    @ApiModelProperty("字段类型(Number、String、Date")
    private String fieldType;

    @ApiModelProperty("规则类型")
    private RuleEnum ruleType;

    @ApiModelProperty("限制规则的值")
    private String ruleValue;

    //删除标识
    private String delFlag;

    public static CleanRule getCleanRule(CleanRuleVo cleanRuleVo){
        CleanRule cleanRule = new CleanRule();
        BeanUtils.copyProperties(cleanRuleVo,cleanRule);
        return cleanRule;
    }
}
