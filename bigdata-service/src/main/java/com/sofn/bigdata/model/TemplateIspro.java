package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

@TableName("TEMPLATE_ISPRO")
@Data
public class TemplateIspro extends BaseModel<TemplateIspro> {

    @TableId(type= IdType.UUID)
    private String id;
    private String fieldname;
    private String fieldcode;
    private String fieldtype;
    private String state;
    private String ispro;
}
