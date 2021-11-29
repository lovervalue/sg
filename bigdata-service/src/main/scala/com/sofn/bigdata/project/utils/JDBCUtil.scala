package com.sofn.bigdata.project.utils

import java.sql.Connection
import java.util.Properties
import javax.sql.DataSource
import org.apache.commons.dbcp.{BasicDataSource, BasicDataSourceFactory}

object JDBCUtil {
  private var dbcpPoor: DataSource = null

  /**
    * 获取连接对象
    */
  def getConnection(env:String) = {
    if (dbcpPoor == null) {
      val properties = PropertiesUtil.getProperties(JDBCUtil.getClass.getClassLoader,env, "db.properties")
      //通过工厂的方法创建连接池对象
      dbcpPoor = BasicDataSourceFactory.createDataSource(properties);
      dbcpPoor.asInstanceOf[BasicDataSource].setDefaultTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ)
    }
    dbcpPoor.getConnection();
  }

}