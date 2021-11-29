package com.sofn.bigdata.sysapi.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class SysFile {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 所属系统
     */
    private String system;

    /**
     * 下载次数
     */
    private Integer downloadTimes;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人员
     */
    private String operator;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 接口号  使用接口的时候会分配
     */
    private String interfaceNum;

    /**
     * 激活状态
     */
    private String activationState;
}
