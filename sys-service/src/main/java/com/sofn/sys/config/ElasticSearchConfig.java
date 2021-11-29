package com.sofn.sys.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author heyongjie
 * @date 2020/9/16 15:58
 */
@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${sofn.elasticsearch.ip}")
    private String ip;

    @Value("${sofn.elasticsearch.host}")
    private String host;

    @Bean
    public RestClient restClient() {
        RestClientBuilder restClientBuilder = RestClient.builder(
//                new HttpHost("192.168.21.128", 9200, "http")
                new HttpHost(ip, Integer.parseInt(host), "http")
        );
        restClientBuilder.setFailureListener(new RestClient.FailureListener(){
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                log.error("ElasticSearch 节点初始化失败：{}", node);
            }
        });
        return restClientBuilder.build();
    }

}
