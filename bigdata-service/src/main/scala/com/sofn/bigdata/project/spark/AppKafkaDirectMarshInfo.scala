package com.sofn.bigdata.project.spark

import com.sofn.bigdata.project.dao.MarshInfoMonitorCountDAO
import com.sofn.bigdata.project.domain.{MarshInfo, MarshInfoMonitorCount}
import com.sofn.bigdata.project.utils.{JDBCUtil, PropertiesUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/**
  * 使用Spark Streaming处理Kafka过来的数据
  */
object AppKafkaDirectMarshInfo {
  val log = org.apache.log4j.LogManager.getLogger("KafkaDirectMarshInfo")

  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      System.err.println("Usage: KafkaDirectMarshInfo <environment>, dev or prod")
      System.exit(1)
    }
    val Array(env) = args
    val ssc = createStreamingContext()

    //创建kafkaParams, zkClient, zkOffsetPath,topicsSet
    val properties = PropertiesUtil.getProperties(AppKafkaDirectMarshInfo.getClass.getClassLoader, env,"bigdata.properties")
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> properties.getProperty("bootstrap.servers"),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> properties.getProperty("group.id"),
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topicsSet = properties.getProperty("topics").split(",").toSet
    val hdfsPath = properties.getProperty("hdfsPath")

    //创建DirectStream
    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topicsSet, kafkaParams)
    )
    //开始处理数据
    messages.foreachRDD((rdd) => {
      if (!rdd.isEmpty()) {
        val offsetRange = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

        dataHandle(env, hdfsPath, rdd)

        //更新每个批次的偏移量到zk中，注意这段代码是在driver上执行的
        for (o <- offsetRange) {
          println(s"${o.topic}  ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        }
        messages.asInstanceOf[CanCommitOffsets].commitAsync(offsetRange)
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }


  private def dataHandle(env: String, hdfsPath: String, rdd: RDD[ConsumerRecord[String, String]]) = {
    try{
      val sparkSession = SparkSessionSingleton.getInstance(rdd.sparkContext.getConf)
      import sparkSession.implicits._
      val list = rdd.map(line => {
        val infos = line.value().split("\t")
        MarshInfo(infos(0), infos(1), infos(2), infos(3), infos(4), infos(5), infos(6), infos(7), infos(8))
      }).toDF()

      //保存一份数据到hdfs,用于后续的相关离线任务处理
      list.coalesce(1).write.mode(SaveMode.Append)
        .partitionBy("siteId", "day")
        .save(hdfsPath)

      list.createOrReplaceTempView("tmpMarshInfo")
      val sql = "select siteId,workStage,targetParam,identifier, first(paramUnit) as paramUnit, max(monitorTime) as monitorTime, " +
        "first(currentValue) as currentValue, first(collectFrequency) as collectFrequency,sum(currentValue) as totalValue,count(currentValue) as colCount " +
        "from (select *from tmpMarshInfo order by monitorTime desc) a  GROUP BY siteId,workStage,targetParam,identifier"

      val df = sparkSession.sql(sql).as[MarshInfoMonitorCount]
      df.foreachPartition(iterator => {
        val conn = JDBCUtil.getConnection(env)
        MarshInfoMonitorCountDAO.write(iterator, conn)
      })
    }catch {
      case ex: Exception => {
        ex.printStackTrace()
      }
    }
  }

  /** *
    * 创建StreamingContext
    */
  def createStreamingContext(): StreamingContext = {
    val sparkConf = new SparkConf()
    sparkConf.setAppName("KafkaDirectMarshInfo")
    sparkConf.setMaster("local[2]")
    sparkConf.set("spark.default.parallelism", "3")
    sparkConf.set("spark.streaming.backpressure.enabled", "true") //激活削峰功能
    sparkConf.set("spark.streaming.backpressure.initialRate", "5000") //第一次读取的最大数据值
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "2000") //每个进程每秒最多从kafka读取的数据条数
    new StreamingContext(sparkConf, Seconds(10))
  }


  object SparkSessionSingleton{
    @transient  private var instance: SparkSession = _
    def getInstance(sparkConf: SparkConf): SparkSession = {
      if (instance == null) {
        instance = SparkSession
          .builder
          .config(sparkConf)
          .getOrCreate()
      }
      instance
    }
  }

}
