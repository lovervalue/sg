package com.sofn.bigdata.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sofn.bigdata.db.JdbcTemplate;
import com.sofn.bigdata.enums.DataCacheKeyEnum;
import com.sofn.bigdata.utils.CacheUtils;
import com.sofn.bigdata.vo.SysInterfaceManageVo;
import com.sofn.common.model.Result;
import com.sofn.common.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DataShareJob implements Job{

    @Autowired
    private RestTemplate restTemplate;

    /**
     * job主任务
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date().toString());
        SysInterfaceManageVo aszdqqInterByCache = CacheUtils.getAszdqqInterByCache();
        if(null == aszdqqInterByCache){
            log.info(new Date().toString() + "=========" + "DataShareJob启动异常:[aszdqqInterByCache == null]");
            return;
        }

        Result<Map> result = sendRequest(aszdqqInterByCache);
        Map resData = result.getData();
        if(null == resData){
            log.info(result.getMsg() + "=========请求失败:[result.getData() == null]");
            return;
        }
        log.info(result.getMsg() + "=========" + JSON.toJSONString(result.getData()));
        try {
            saveData(resData,aszdqqInterByCache);
        }catch (Exception e){
            log.info("共享接口数据入库失败:" + e.getMessage());
        }

    }

    /**
     * 数据入库
     * @param resData
     */
    private void saveData(Map resData, SysInterfaceManageVo aszdqqInterByCache) {
        String returnParams = aszdqqInterByCache.getReturnPar();
        String tableName = DataCacheKeyEnum.ASZDQQ_INTER_KEY.getCode();
        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(returnParams));

        //1 检查表tableName是否已存在数据库中，如果存在，插入数据，如果不存在则创建数据表
        //2 遍历resData，插入数据

        if(!isExist(tableName)){
            createTable(tableName,params);
        }

        StringBuilder insertSql = new StringBuilder("insert into ").append(tableName).append(" (");
        StringBuilder valueSql = new StringBuilder("(");
        for (String key : params.keySet()) {
            insertSql.append(key).append(",");
            valueSql.append(params.get(key)).append(",");
        }
        insertSql.deleteCharAt(insertSql.length() - 1).append(")");
        String sqlInit = insertSql.toString();
        valueSql.deleteCharAt(valueSql.length() - 1).append(")");
        insertSql.append(" values ").append(valueSql);
        JdbcTemplate.save(insertSql.toString());

        StringBuilder dataSql = new StringBuilder("(");
        for (Object key : resData.keySet()) {
            dataSql.append(params.get(key)).append(",");
        }
        dataSql.deleteCharAt(dataSql.length() - 1).append(")");
        JdbcTemplate.save(sqlInit + dataSql);
    }

    /**
     * 创建tableName
     * @param tableName
     * @param params
     */
    private void createTable(String tableName, JSONObject params) {
        StringBuilder sb = new StringBuilder("create table " + tableName + "(");
        sb.append("id").append(" VARCHAR(32) not null,");
        for(String key : params.keySet()){
            sb.append(key).append(" VARCHAR(32),");
        }
        sb.deleteCharAt(sb.length()-1).append(");");
        JdbcTemplate.save(sb.toString());
    }

    /**
     * 检查数据库中是否已存在tableName
     * @param tableName
     * @return
     */
    private boolean isExist(String tableName) {
        String sql = "select 1 from all_all_tables where table_name = '" + tableName + "'";
        Object query = JdbcTemplate.query(sql, null);
        return query != null;
    }

    /**
     * 发起请求
     * @param manageVo
     * @return
     */
    private Result<Map> sendRequest(SysInterfaceManageVo manageVo) {

        try {
            String url = manageVo.getInterUrl();//获取链接
            Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(manageVo.getRequestPar()));//获取参数
            Map<String, String> header = new HashMap<>();//获取请求头

            String jsonData = manageVo.getRequestPar();//获取请求体

            header.put("Authorization",UserUtil.getLoginToken()); //设置请求头，获取当前登录用户的token
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(header);

            HttpEntity<String> HttpEntity = new HttpEntity<>(headers);//构建请求头
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);//动态拼接url

            if (!params.isEmpty()) {  //get
                for (Map.Entry<String, Object> kv : params.entrySet()) {
                    //拼接好参数后的URltest.com/url?param1={param1}&param2={param2}
                    builder.queryParam(kv.getKey(), kv.getValue());
                }
                url = builder.build().toString();//获得最终拼接的url
            }
            if ("GET".equalsIgnoreCase(manageVo.getRequestModel())){
                //尝试get请求
                ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.GET, HttpEntity, Map.class);
                Map map = exchange.getBody();
                return Result.ok(map);
            }

            if ("POST".equalsIgnoreCase(manageVo.getRequestModel())){
                HttpEntity<String> entity = new HttpEntity<String>(jsonData,headers);
                ResponseEntity<Map> exchange1 = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
                Map map = exchange1.getBody();

                return Result.ok(map);
            }

            return Result.error("启动失败");
        } catch (RestClientException e) {
            e.printStackTrace();
            return Result.error("启动失败");
        }


    }

}
