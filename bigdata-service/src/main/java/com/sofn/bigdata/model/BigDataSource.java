package com.sofn.bigdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sofn.common.model.BaseModel;
import lombok.Data;

@TableName("DB_SOURCE")
@Data
public class BigDataSource extends BaseModel<BigDataSource> {

    @TableId(type= IdType.UUID)
    private String id;
    private String name;
    private String alias;
    private String driverType;
    private String driverClass;
    private String conString;
    private String validateType;
    private String username;
    private String password;
    private String dbsourceChar;
    private String dbtargetChar;
    private String conMaxNum;
    private String checkSqlId;
    private String isolevel;
    private String quotedIdent;
    private String dbcomment;

}
