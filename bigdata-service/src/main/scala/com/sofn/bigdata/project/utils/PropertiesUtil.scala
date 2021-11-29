package com.sofn.bigdata.project.utils

import java.io.InputStreamReader
import java.util.Properties

object PropertiesUtil {
  def getProperties(classLoader: ClassLoader, path:String, fileName:String)={
    val properties = new Properties();
    val inputStreamReader = new InputStreamReader(classLoader.getResourceAsStream(path+"/"+fileName),"UTF-8")
    properties.load(inputStreamReader)
    properties
  }
}
