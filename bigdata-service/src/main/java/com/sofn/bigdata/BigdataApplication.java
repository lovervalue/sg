package com.sofn.bigdata;

import com.sofn.bigdata.mqtt.ClientMQTT;

import com.sofn.bigdata.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Slf4j
@ComponentScan("com.sofn")
@SpringBootApplication(scanBasePackages = "com.sofn")
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
public class BigdataApplication {

    @Value("${idWorker.workId}")
    private Integer workId;
    @Value("${idWorker.dataCenterId}")
    private Integer dataCenterId;

    public static void main(String[] args) {

        /*SpringApplication.run(BigdataApplication.class, args);*/
        Environment env = SpringApplication.run(BigdataApplication.class, args).getEnvironment();

        //同时启动mqtt服务
        ClientMQTT mqtt = new ClientMQTT(env.getProperty("kafka.metadata.broker.list"),
                env.getProperty("kafka.topic"),
                env.getProperty("mqtt.host"),
                env.getProperty("mqtt.clientid"),
                env.getProperty("mqtt.userName"),
                env.getProperty("mqtt.passWord"));
        mqtt.start();
    }

    @Bean("SnowflakeIdWorker")
    public SnowflakeIdWorker getSnowflakeIdWorker(){
        return new SnowflakeIdWorker(workId, dataCenterId);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SchedulerFactory getSchedulerFactory(){
        return new StdSchedulerFactory();
    }

}
