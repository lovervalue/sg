package com.sofn.bigdata.entities;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class MarshInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 工段
     */
    @NotEmpty(message = "工段信息必填")
    private String workStage;

    /**
     * 目标参数
     */
    @NotEmpty(message = "参数必填")
    private String targetParam;

    /**
     * 目标参数的单位，如流量(m3/h)、压力(kPa)等
     */
    @NotEmpty(message = "参数单位必填")
    private String paramUnit;

    /**
     * 监测时间 yyyy-MM-dd HH:mm:ss
     */
    @NotEmpty(message = "监测时间必填")
    private String monitorTime;

    /**
     * 当前值
     */
    @NotEmpty(message = "当前值必填")
    private String currentValue;

    /**
     * 采集频率
     */
    private String collectFrequency;

    /**
     * 站点编码
     */
    private String siteId;

    /**
     * 监测日期， 该字段用于分区 如20191125
     */
    private String day;

    /**
     * 采集设备编号
     */
    private String identifier;

    /**
     * 设施编号
     */
    private String facilityno;

    /**
     * 监测设备编号
     */
    private String mdevno;


    @Override
    public String toString(){
        return siteId+"\t"+
                workStage+"\t"+
                targetParam+"\t"+
                identifier+"\t"+
                facilityno+"\t"+
                mdevno+"\t"+
                paramUnit+"\t"+
                monitorTime+"\t"+
                currentValue+"\t"+
                collectFrequency +"\t"+
                day;
    }

    public static void main(String[] args) {
        MarshInfo marshInfo = new MarshInfo();
        marshInfo.siteId = "1001";
        marshInfo.workStage = "沼气存储";
        marshInfo.targetParam = "储量";
        marshInfo.identifier = "1#";
        marshInfo.facilityno = "2#";
        marshInfo.mdevno = "3#";
        marshInfo.paramUnit = "%";
        marshInfo.monitorTime = "2020-01-01 12:00:15";
        marshInfo.currentValue = "17.4";
        marshInfo.collectFrequency="15min";

        System.out.println(JSON.toJSONString(marshInfo));
//"{\"collectFrequency\":\"15min\",\"currentValue\":\"17.4\",\"identifier\":\"1#\",\"monitorTime\":\"2020-01-01 12:00:15\",\"paramUnit\":\"%\",\"siteId\":\"1001\",\"targetParam\":\"储量\",\"workStage\":\"沼气存储\"}"
    }
}
