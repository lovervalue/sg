package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value="非结构化数据分析Vo对象")
public class JsonAnalyzeVo {

    private String id;
    private String fileName;
    private String fileAlias;
    private String fileSource;
    private String operator;
    private Date operatDate;

}
