package com.sofn.bigdata.enums;

import com.sofn.bigdata.vo.DataEnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接方式集合
 */
@Getter
public enum RelationEnum {

    INNER_JOIN("inner_join","内连接(inner join)"),
    LEFT_JOIN("left_join","左连接(left join)"),
    RIGHT_JOIN("right_join","右连接(right join)"),
    OUTER_JOIN("outer_join","全连接(outer join)");

    private String code;
    private String desc;

    RelationEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获得所有的枚举值和描述信息
     * @return
     */
    public static List<DataEnumVo> getEnumInfo(){
        List<DataEnumVo> list = new ArrayList<>();
        for(RelationEnum e: RelationEnum.values()){
            DataEnumVo obj = new DataEnumVo();
            obj.setCode(e.getCode());
            obj.setDesc(e.getDesc());
            list.add(obj);
        }
        return list;
    }

    /**
     * 根据code获得描述信息
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        for(RelationEnum e: RelationEnum.values()){
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

}
