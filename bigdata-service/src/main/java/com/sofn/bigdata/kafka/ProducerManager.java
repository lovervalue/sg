package com.sofn.bigdata.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerManager{
    //1.加载类即创建线程池
    private static int threadCount = 8;
    public static ExecutorService pool;
    static {
        pool = Executors.newFixedThreadPool(threadCount);
    }


    //创建一个集合，控制生产者的数量，通过index获取生产者
    private static int producerIndex = 0;
    private static int producerCount=50;
    private static List<KafkaProducer> producerList = new ArrayList<>();

    public synchronized static MyKafkaProducer getProducer(String topic, String brokerList, List<String> list){
        if(producerIndex>=producerCount){
            producerIndex = producerIndex-producerCount;
        }
        if(producerList.size()==0){
            for(int i=0;i<producerCount;i++){
                KafkaProducer kafkaProducer = createProducer(brokerList);
                producerList.add(kafkaProducer);
            }
        }
        System.out.println("producerIndex: "+producerIndex);
        return new MyKafkaProducer(producerList.get(producerIndex++),topic, list);
    }

    private static KafkaProducer createProducer(String brokerList) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerList);
        properties.put("acks","1");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        properties.put("partitioner.class","com.sofn.eep.bigdatamarsh.kafka.MarshInfoPartition");
        return new KafkaProducer(properties);
    }
}
//