package com.sofn.bigdata.enums;

import com.sofn.bigdata.vo.DataEnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符集集合
 */
@Getter
public enum CharSetEnum {

    CHAR_GBK("1","GBK"),
    CHAR_BIG5("2","BIG5"),
    CHAR_ISO_8859_1("3","ISO-8859-1"),
    CHAR_UTF_8("4","UTF-8"),
    CHAR_UTF_16("5","UTF-16");

    private String code;
    private String desc;

    CharSetEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获得所有的枚举值和描述信息
     * @return
     */
    public static List<DataEnumVo> getEnumInfo(){
        List<DataEnumVo> list = new ArrayList<>();
        for(CharSetEnum e: CharSetEnum.values()){
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
        for(CharSetEnum e: CharSetEnum.values()){
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

}
