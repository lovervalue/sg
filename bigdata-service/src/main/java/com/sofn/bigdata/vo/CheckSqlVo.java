package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="校验语句Vo对象")
public class CheckSqlVo {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("校验条件")
    private String checkname;

    @ApiModelProperty("校验sql语句")
    private String checksql;

    public CheckSqlVo(String id,String checkname,String checksql){
        this.id = id;
        this.checkname = checkname;
        this.checksql = checksql;
    }

}
