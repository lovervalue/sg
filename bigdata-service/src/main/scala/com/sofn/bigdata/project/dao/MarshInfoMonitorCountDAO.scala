package com.sofn.bigdata.project.dao

import java.sql.{Connection, PreparedStatement, Timestamp}
import java.util.{Date, UUID}

import com.sofn.bigdata.project.domain.MarshInfoMonitorCount
import com.sofn.bigdata.project.utils.DateUtil

/**
  * 数据访问层
  */
object MarshInfoMonitorCountDAO {

  val querySql = "SELECT *FROM ASTCAP_AUTO_COLLECT WHERE SITE_ID=? AND SECTION_ID=? AND CONFIG_ID=? AND IDENTIFIER=?"
  val insertSql = "INSERT INTO ASTCAP_AUTO_COLLECT(ID,SITE_ID,SECTION_ID,CONFIG_ID,IDENTIFIER,CONFIG_UNIT,COLLECT_FRE,CUR_VALUE,MONITOR_TIME,TOTAL_VALUE,COL_COUNT,CREATE_USER_ID,CREATE_TIME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)"
  val updateSql = "UPDATE ASTCAP_AUTO_COLLECT SET CUR_VALUE=?,MONITOR_TIME=?,TOTAL_VALUE=?,COL_COUNT=?,CONFIG_UNIT=?,COLLECT_FRE=?,UPDATE_USER_ID=?,UPDATE_TIME=? WHERE SITE_ID=? AND SECTION_ID=? AND CONFIG_ID=? AND IDENTIFIER=?"
  /**
    * 将数据写到Oracle
    */
  def write(iterator: Iterator[MarshInfoMonitorCount], conn: Connection): Unit = {
    conn.setAutoCommit(false)
    val queryStatement = conn.prepareStatement(querySql)
    val insertStatement = conn.prepareStatement(insertSql)
    val updateStatement = conn.prepareStatement(updateSql)
    while(iterator.hasNext){
      val ele = iterator.next()
      val queryResult = query(queryStatement, ele)
      //已经存在,修改； 若不存在，插入
      if (queryResult.next()) {
        val newTotalValue = queryResult.getDouble("TOTAL_VALUE") + ele.totalValue.toDouble
        val newColCount = queryResult.getInt("COL_COUNT") + ele.colCount.toInt
        val historyTimestamp = queryResult.getTimestamp("MONITOR_TIME").getTime
        val historyValue = queryResult.getString("CUR_VALUE")
        update(updateStatement, ele, newTotalValue, newColCount,historyTimestamp, historyValue)
      } else {
        insert(insertStatement, ele)
      }
    }
    updateStatement.executeBatch()
    insertStatement.executeBatch()
    conn.commit()
    conn.close()
  }


  /**
    * 查询某沼气站点下，某个工段下的某个目标参数的统计信息
    */
  def query(queryStatement: PreparedStatement, ele: MarshInfoMonitorCount) = {
    queryStatement.setString(1, ele.siteId)
    queryStatement.setString(2, ele.workStage)
    queryStatement.setString(3, ele.targetParam)
    queryStatement.setString(4,
      if(ele.identifier==null||ele.identifier.trim.eq("")||ele.identifier=="null") "default" else ele.identifier)
    queryStatement.executeQuery()
  }

  /**
    * 更新某沼气站点下，某个工段下的某个目标参数的统计信息
    */
  def update(updateStatement: PreparedStatement, ele: MarshInfoMonitorCount,
             newTotalValue: Double, newColCount: Int,
             historyTimestamp:Long, historyValue: String) = {

    //如果更新批次的最新时间比原数据库的时间更早，保留原始值，和原始监测时间
    val currentTimestamp = DateUtil.YYYYMMDDHHMMSS_FORMAT.parse(ele.monitorTime).getTime
    if(currentTimestamp>historyTimestamp){
      updateStatement.setString(1, ele.currentValue)
      updateStatement.setTimestamp(2, new Timestamp(currentTimestamp))
    }else{
      updateStatement.setString(1, historyValue)
      updateStatement.setTimestamp(2, new Timestamp(historyTimestamp))
    }
    updateStatement.setDouble(3, newTotalValue)
    updateStatement.setInt(4, newColCount)
    updateStatement.setString(5, ele.paramUnit)
    updateStatement.setString(6, ele.collectFrequency)

    //插入当前时间和缺省用户
    updateStatement.setString(7, "defaultUser")
    updateStatement.setTimestamp(8, new Timestamp(new Date().getTime))
    updateStatement.setString(9, ele.siteId)
    updateStatement.setString(10, ele.workStage)
    updateStatement.setString(11, ele.targetParam)
    updateStatement.setString(12,
      if(ele.identifier==null||ele.identifier.trim.eq("")||ele.identifier=="null") "default" else ele.identifier)

    updateStatement.addBatch()
  }

  /**
    * 新增某沼气站点下，某个工段下的某个目标参数的统计信息
    */
  def insert(insertStatement: PreparedStatement, ele: MarshInfoMonitorCount) = {
    insertStatement.setString(1, UUID.randomUUID().toString().replaceAll("-",""))
    insertStatement.setString(2, ele.siteId)
    insertStatement.setString(3, ele.workStage)
    insertStatement.setString(4, ele.targetParam)
    insertStatement.setString(5,
      if(ele.identifier==null||ele.identifier.trim.eq("")||ele.identifier=="null")"default" else ele.identifier)
    insertStatement.setString(6, ele.paramUnit)
    insertStatement.setString(7, ele.collectFrequency)
    insertStatement.setString(8, ele.currentValue)
    insertStatement.setTimestamp(9, new Timestamp(DateUtil.YYYYMMDDHHMMSS_FORMAT.parse(ele.monitorTime).getTime))
    insertStatement.setString(10, ele.totalValue)
    insertStatement.setString(11, ele.colCount)

    //插入当前时间和缺省用户
    insertStatement.setString(12, "defaultUser")
    insertStatement.setTimestamp(13, new Timestamp(new Date().getTime))
    insertStatement.addBatch()
  }

}
