package com.sofn.bigdata.enums;

import com.sofn.bigdata.vo.DataEnumVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据共享接口状态
 */
@Getter
public enum DataShareStatusEnum {

    NOTSTARTED("0","禁止"),
    STARTED("1","启动");

    private String code;
    private String desc;

    DataShareStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获得所有的枚举值和描述信息
     * @return
     */
    public static List<DataEnumVo> getEnumInfo(){
        List<DataEnumVo> list = new ArrayList<>();
        for(DataShareStatusEnum e: DataShareStatusEnum.values()){
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
        for(DataShareStatusEnum e: DataShareStatusEnum.values()){
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
