package com.sofn.bigdata.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;

public class MyKafkaProducer extends Thread{
    KafkaProducer producer;
    private String topic;
    private List<String> marshInfos;

    public MyKafkaProducer(KafkaProducer kafkaProducer, String topic, List<String> marshInfos) {
        this.producer = kafkaProducer;
        this.topic = topic;
        this.marshInfos = marshInfos;
    }

    @Override
    public void run(){
        try {
            for(String marshInfo: marshInfos){
                String key = marshInfo.split("\\t")[0];
                producer.send(new ProducerRecord(topic,key, marshInfo.toString()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
