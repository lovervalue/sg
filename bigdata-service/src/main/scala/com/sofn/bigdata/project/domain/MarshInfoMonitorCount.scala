package com.sofn.bigdata.project.domain

/**
  * 沼气站点日志的统计信息
  *
  * @param siteId           沼气站点编码
  * @param workStage        工段
  * @param targetParam      工段对应的参数
  * @param paramUnit        参数单位
  * @param monitorTime      最新监测时间
  * @param currentValue     最新值
  * @param collectFrequency 采集频率
  * @param totalValue       累计值
  * @param colCount         采集次数
  */
case class MarshInfoMonitorCount(siteId: String,
                                 workStage: String,
                                 targetParam: String,
                                 identifier: String,
                                 paramUnit: String,
                                 monitorTime: String,
                                 currentValue: String,
                                 collectFrequency: String,
                                 totalValue: String,
                                 colCount: String) extends Serializable