package com.sofn.bigdata.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sofn.bigdata.config.QuartzServletContextListener;
import com.sofn.bigdata.mapper.TemplateMapper;
import com.sofn.bigdata.mapper.WaterQualityMapper;
import com.sofn.bigdata.model.Template;
import com.sofn.bigdata.model.WaterQuality;
import com.sofn.bigdata.utils.Crawlers;
import com.sofn.bigdata.utils.SnowflakeIdWorker;
import com.sofn.common.exception.SofnException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:袁彬锋
 * @date:2020/5/28 10:17
 * @company:曙光集团绿康元公司
 * @description:
 */
@Component
public class WaterQualityJob implements Job {

    private WaterQualityMapper waterQualityMapper;
    private SnowflakeIdWorker snowflakeIdWorker;
    private TemplateMapper templateMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        before(jobExecutionContext);
        Long templateId = jobExecutionContext.getTrigger().getJobDataMap().getLong("templateId");
        try {
            //说明需要启动定时任务爬取数据
            QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("url").in("id", templateId);
            List<Map<String, Object>> maps = templateMapper.selectMaps(queryWrapper);
            if (maps == null  || maps.size() == 0){
                throw new SofnException("未查询到相应爬取模板信息");
            }
            Map<String, Object> map = maps.get(0);
            if (map == null){
                throw new SofnException("未查询到相应爬取模板信息");
            }
            String url = (String) map.get("url");
            //例如：String url = "http://123.127.175.45:8082/ajax/GwtWaterHandler.ashx";

            String param = "Method=SelectRealData";//这里属于硬编码
            List<WaterQuality> waterQualityList = null;
            try {
                String str = Crawlers.sendPost(url, param);
                String s = str.replace("--", "");
                waterQualityList = JSON.parseArray(s, WaterQuality.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SofnException("错误的url或返回值解析异常");
            }
            Date date = new Date();
            for (WaterQuality waterQuality : waterQualityList) {
                long id = snowflakeIdWorker.nextId();
                waterQuality.setId(id);
                waterQuality.setCrawlingDate(date);
                waterQuality.setCrawlingTime(date);
                waterQuality.setTemplateId(templateId);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<WaterQuality> waterQualitys = waterQualityMapper.selectListByCrawlingDate(simpleDateFormat.format(date));
            if (waterQualitys != null && waterQualitys.size() > 0) {
                List<Long> ids = new ArrayList<Long>();
                for (WaterQuality waterQuality : waterQualitys) {
                    ids.add(waterQuality.getId());
                }
                waterQualityMapper.deleteBatchIds(ids);
            }
            waterQualityMapper.batchInsert(waterQualityList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SofnException("数据爬取异常", 503);
        }
    }

    public void before(JobExecutionContext jobContext){
        ServletContext context = null;
        try {
            context = (ServletContext) jobContext.getScheduler().getContext()
                    .get(QuartzServletContextListener.MY_CONTEXT_NAME);
        } catch (SchedulerException e1) {
            e1.printStackTrace();
        }
        WebApplicationContext cxt = (WebApplicationContext) context.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        snowflakeIdWorker = (SnowflakeIdWorker) cxt.getBean("SnowflakeIdWorker");
        waterQualityMapper = (WaterQualityMapper) cxt.getBean("waterQualityMapper");
        templateMapper = (TemplateMapper) cxt.getBean("TemplateMapper");
    };
}
