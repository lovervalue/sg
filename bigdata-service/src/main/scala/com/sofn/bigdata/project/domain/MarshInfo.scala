package com.sofn.bigdata.project.domain

/**
  * 沼气站点日志信息
  *
  * @param siteId           沼气站点编码
  * @param workStage        工段
  * @param targetParam      工段对应的参数
  * @param paramUnit        参数单位
  * @param monitorTime      监测时间
  * @param currentValue     当前值
  * @param collectFrequency 采集频率
  * @param day              监测时间对应的天 如20200101
  */
case class MarshInfo(siteId: String,
                     workStage: String,
                     targetParam: String,
                     identifier: String,
                     paramUnit: String,
                     monitorTime: String,
                     currentValue: String,
                     collectFrequency: String,
                     day: String)
