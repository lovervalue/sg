package com.sofn.bigdata.config;

import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.job.WaterQualityJob;
import com.sofn.bigdata.mapper.TemplateMapper;
import com.sofn.bigdata.mapper.WaterQualityMapper;
import com.sofn.bigdata.model.Template;
import com.sofn.bigdata.model.WaterQuality;
import com.sofn.common.exception.SofnException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * @author:袁彬锋
 * @date:2020/5/28 10:39
 * @company:曙光集团绿康元公司
 * @description:
 */
@Configuration
public class QuartzConfig {
    private Long waterQualityTemplateId = Constant.WATER_QUALITY_TEMPLATE_ID;
    public static final String EXECUTE_CYCLE = "executeCycle";

    @Autowired
    private SchedulerFactory schedulerFactory;
    @Autowired
    private TemplateMapper templateMapper;

    @Bean("waterQualityScheduler")
    public Scheduler getWaterQualityScheduler(){
        String executeCycle = before();//获取模板设定的执行周期
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail waterQualityJobDetail = JobBuilder.newJob(WaterQualityJob.class).withIdentity(Constant.WATER_QUALITY_JOB_NAME).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(Constant.WATER_QUALITY_TRIGGER_NAME).withSchedule(CronScheduleBuilder.cronSchedule(executeCycle).withMisfireHandlingInstructionDoNothing()).usingJobData("templateId", waterQualityTemplateId).build();
            scheduler.scheduleJob(waterQualityJobDetail, trigger);
            scheduler.getContext().put(EXECUTE_CYCLE, executeCycle);
            after();
            return scheduler;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SofnException("创建水质数据爬取任务调度出现异常");
        }
    }

    private String before(){
        Template template = templateMapper.selectById(waterQualityTemplateId);
        return template.getExecuteCycle();
    }

    private void after(){
        Template template = new Template();
        template.setId(waterQualityTemplateId);
        template.setStatus(Constant.UP);
        templateMapper.updateById(template);
    }
}
