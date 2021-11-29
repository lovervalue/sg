package com.sofn.bigdata.project.utils

import redis.clients.jedis._

object RedisUtil {
  private var jedisCluster:JedisCluster=null

  def getRedisConn(env:String): JedisCluster = {
    if(jedisCluster == null){
      val properties = PropertiesUtil.getProperties(RedisUtil.getClass.getClassLoader, env, "db.properties")
      val jedisClusterNodes = new java.util.HashSet[HostAndPort]()
      val redisClusterNodes = properties.getProperty("redis.cluster.nodes")
      redisClusterNodes.split(",").foreach(HostAndPort=>{
        val arr = HostAndPort.split(":")
        jedisClusterNodes.add(new HostAndPort(arr(0),arr(1).toInt))
      })
      jedisCluster = new JedisCluster(jedisClusterNodes)
    }
    jedisCluster
  }
}
