package com.sofn.bigdata.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author:袁彬锋
 * @date:2020/4/27 9:20
 * @company:曙光集团绿康元公司
 * @description:连接池配置，httpClient的获取，请求对象配置对象
 */

@Component
public class HttpClientConf {

    @Value("${http.maxTotal}")
    private Integer maxTotal;

    @Value("${http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${http.connectTimeout}")
    private Integer connectTimeout;

    @Value("${http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${http.socketTimeout}")
    private Integer socketTimeout;

    @Value("${http.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;

    //获取httpClient连接池管理器
    @Bean("httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return connectionManager;
    }

    //获取httpClient客户端
    @Bean
    public CloseableHttpClient getHttpClient(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager connectionManager){
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    //获取请求配置对象
    @Bean
    public RequestConfig getRequestConfig(){
        return RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }
}
