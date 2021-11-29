package com.sofn.bigdata.utils;

/**
 * @Acthor Tao.Lee @date 2021/3/8 13:52
 * @Description
 */
import lombok.Data;

/**
 * @ClassName Sort
 * @Description TODO
 * @Author yb
 * @Date 2019/11/12/01219:29
 * @Version 1.0
 **/
@Data
public class Sort {
    private String order;
    private String Field;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }
}