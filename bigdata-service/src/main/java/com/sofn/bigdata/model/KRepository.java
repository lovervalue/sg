package com.sofn.bigdata.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Acthor Tao.Lee @date 2021/2/2 16:22
 * @Description kettle资源库连接实体类
 */
public class KRepository {

    private Integer repositoryId ;
    //添加者
 //   private Integer addUser ;
    //是否删除（1：存在；0：删除）
    private Integer delFlag ;
    //编辑者
//    private Integer editUser ;
    //资源库数据库访问模式（"Native", "ODBC", "OCI", "Plugin", "JNDI")
    @ApiModelProperty("资源库数据库访问模式（\"Native\", \"ODBC\", \"OCI\", \"Plugin\", \"JNDI\")")
    private String databaseAccess ;
    //资源库数据库主机名或者IP地址
    private String databaseHost ;
    //资源库数据库名称
    @ApiModelProperty("资源库数据库名称")
    private String databaseName ;
    //数据库登录密码
    @ApiModelProperty("数据库登录密码")
    private String databasePassword ;
    //资源库数据库端口号
    private String databasePort ;
    //数据库登录账号
    @ApiModelProperty("数据库登录账号")
    private String databaseUsername ;
    //资源库名称
    @ApiModelProperty("资源库名称")
    private String repositoryName ;
    //登录密码
    @ApiModelProperty("登录密码")
    private String repositoryPassword ;
    @ApiModelProperty("资源库数据库类型如（MYSQL、ORACLE）还有很多，不用列表，直接填")
    //资源库数据库类型（MYSQL、ORACLE）
    private String repositoryType ;
    //登录用户名
    @ApiModelProperty("登录用户名admin")
    private String repositoryUsername ;
    //添加时间
    private Date addTime ;

    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDatabaseAccess() {
        return databaseAccess;
    }

    public void setDatabaseAccess(String databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryPassword() {
        return repositoryPassword;
    }

    public void setRepositoryPassword(String repositoryPassword) {
        this.repositoryPassword = repositoryPassword;
    }

    public String getRepositoryType() {
        return repositoryType;
    }

    public void setRepositoryType(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public String getRepositoryUsername() {
        return repositoryUsername;
    }

    public void setRepositoryUsername(String repositoryUsername) {
        this.repositoryUsername = repositoryUsername;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
