package com.sofn.bigdata.service.impl;

import com.sofn.bigdata.kafka.MyKafkaProducer;
import com.sofn.bigdata.kafka.ProducerManager;
import com.sofn.bigdata.service.MarshInfoService;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "marshInfoService")
public class MarshInfoServiceImpl implements MarshInfoService {
    private static volatile SparkSession spark;

    @Autowired
    private Environment environment;

    @Override
    public void handleData(List<String> marshInfos) {
        //利用kafka + spark streaming，用于实时统计分析
        MyKafkaProducer kafkaProducer = ProducerManager.getProducer(
                environment.getProperty("kafka.topic"),
                environment.getProperty("kafka.metadata.broker.list"),
                marshInfos);
        ProducerManager.pool.submit(kafkaProducer);
    }

}
