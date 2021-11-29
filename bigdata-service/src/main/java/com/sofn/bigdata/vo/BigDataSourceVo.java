package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel(value="数据源Vo对象")
public class BigDataSourceVo {

    @ApiModelProperty(value = "数据源ID")
    @Length(max = 32,message = "数据源ID超长")
    private String id;

    @ApiModelProperty(value = "数据源名称")
    @Length(max = 255,message = "数据源名称超长")
    private String name;

    @ApiModelProperty(value = "数据源别名")
    @Length(max = 255,message = "数据源别名超长")
    private String alias;

    @ApiModelProperty(value = "驱动程序类型ID")
    @Length(max = 32,message = "驱动程序类型ID超长")
    private String driverTypeid;

    @ApiModelProperty(value = "驱动程序类型名称")
    @Length(max = 32,message = "驱动程序类型名称超长")
    private String driverTypename;

    @ApiModelProperty(value = "驱动程序类")
    @Length(max = 255,message = "驱动程序类超长")
    private String driverClass;

    @ApiModelProperty(value = "连接字符串")
    @Length(max = 255,message = "连接字符串超长")
    private String conString;

    @ApiModelProperty(value = "验证方式")
    @Length(max = 32,message = "验证方式超长")
    private String validateType;

    @ApiModelProperty(value = "验证方式名称")
    @Length(max = 32,message = "验证方式名称超长")
    private String validateTypename;

    @ApiModelProperty(value = "用户名")
    @Length(max = 255,message = "用户名超长")
    private String username;

    @ApiModelProperty(value = "密码")
    @Length(max = 255,message = "密码超长")
    private String password;

    @ApiModelProperty(value = "数据源字符集ID")
    @Length(max = 32,message = "数据源字符集ID超长")
    private String dbsourceCharid;

    @ApiModelProperty(value = "数据源字符集名称")
    @Length(max = 32,message = "数据源字符集名称超长")
    private String dbsourceCharname;

    @ApiModelProperty(value = "目标数据库字符集ID")
    @Length(max = 32,message = "目标数据库字符集ID超长")
    private String dbtargetharid;

    @ApiModelProperty(value = "目标数据库字符集名称")
    @Length(max = 32,message = "目标数据库字符集名称超长")
    private String dbtargetharname;

    @ApiModelProperty(value = "最大连接数")
    @Length(max = 32,message = "最大连接数")
    private String conMaxNum;

    @ApiModelProperty(value = "校验语句")
    @Length(max = 32,message = "校验语句ID超长")
    private String checkSqlId;

    @ApiModelProperty(value = "校验语句名称")
    @Length(max = 32,message = "校验语句名称超长")
    private String checkSqlname;

    @ApiModelProperty(value = "校验语句sql")
    @Length(max = 32,message = "校验语句sql超长")
    private String checkSqlvalue;

    @ApiModelProperty(value = "隔离级别")
    @Length(max = 32,message = "隔离级别ID超长")
    private String isolevelid;

    @ApiModelProperty(value = "隔离级别名称")
    @Length(max = 32,message = "隔离级别名称超长")
    private String isolevelname;

    @ApiModelProperty(value = "引用标识符")
    @Length(max = 255,message = "引用标识符超长")
    private String quotedIdent;

    @ApiModelProperty(value = "描述")
    @Length(max = 255,message = "描述超长")
    private String dbcomment;

}
