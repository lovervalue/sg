package com.sofn.bigdata.enums;

import com.sofn.bigdata.vo.EnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举之数据字段清洗方式
 */
@Getter
public enum RuleEnum {
    MORE("0", "大于", "Number"),
    EQUALS("1", "等于", "Number"),
    LESS("2", "小于", "Number"),
    NOT_EMPTY("3", "非空", "String"),
    CONTAINS("4", "包含", "String"),
    NOT_CONTAINS("5", "不包含", "String"),
    EARLIER("6","早于","Date"),
    LATER("7","晚于","Date");

    private String code;
    private String filedType;
    private String desc;

    RuleEnum(String code, String desc, String fieldType) {
        this.code = code;
        this.desc = desc;
        this.filedType = fieldType;
    }

    /**
     * 获得所有的枚举值和描述信息
     *
     * @return
     */
    public static List<EnumVo> getEnumInfo() {
        List<EnumVo> list = new ArrayList<>();
        for (RuleEnum e : RuleEnum.values()) {
            EnumVo obj = new EnumVo();
            obj.setCode(e.getCode());
            obj.setSupply(e.getFiledType());
            obj.setDesc(e.getDesc());
            list.add(obj);
        }
        return list;
    }

    /**
     * 根据code获得描述信息
     *
     * @param code
     * @return
     */
    public static String getDescByCode(String code) {
        for (RuleEnum e : RuleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

}
