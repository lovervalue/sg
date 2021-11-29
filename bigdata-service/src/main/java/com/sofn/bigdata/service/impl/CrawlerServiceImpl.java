package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sofn.bigdata.config.QuartzConfig;
import com.sofn.bigdata.constant.Constant;
import com.sofn.bigdata.mapper.TemplateMapper;
import com.sofn.bigdata.mapper.WaterQualityMapper;
import com.sofn.bigdata.model.Template;
import com.sofn.bigdata.service.CrawlerService;
import com.sofn.common.exception.SofnException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author:袁彬锋
 * @date:2020/5/21 14:38
 * @company:曙光集团绿康元公司
 * @description:
 */
@Slf4j
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Autowired
    @Qualifier("waterQualityScheduler")
    private Scheduler scheduler;
    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public void crawling(Long templateId, String isOpen) {
        //参数判断
        judge(isOpen);

        //爬取程序启动或关闭
        if (templateId.equals(Constant.WATER_QUALITY_TEMPLATE_ID)){
            waterQualityCrawler(templateId, isOpen);//执行水质爬取调度任务
        }
    }

    public void judge(String isOpen){
        if (!isOpen.equals("Y") && !isOpen.equals("N")){
            throw new SofnException("参数异常", 400);
        }

        boolean started = false;
        try {
            started = scheduler.isStarted();//判断是否已经启动
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        if (started){
            if (isOpen.equals("Y")){
                return;
            }
        }else {
            if (isOpen.equals("N")){
                return;
            }
        }
    }

    public void waterQualityCrawler(Long templateId, String isOpen){
        JobKey waterQualityJobKey = new JobKey(Constant.WATER_QUALITY_JOB_NAME);
        Template template = new Template();//修改模板状态
        template.setId(templateId);
        if (isOpen.equals("Y")){
            //启动任务调度
            try {
                String executeCycle = scheduler.getContext().getString("executeCycle");
                QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("execute_cycle").eq("id", templateId);
                List<Map<String, Object>> maps = templateMapper.selectMaps(queryWrapper);
                String execute_cycle = (String) maps.get(0).get(QuartzConfig.EXECUTE_CYCLE);
                if (executeCycle.equals(execute_cycle)){
                    scheduler.resumeJob(waterQualityJobKey);
//                    scheduler.start();
                }else {
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(Constant.WATER_QUALITY_TRIGGER_NAME).withSchedule(CronScheduleBuilder.cronSchedule(execute_cycle)).usingJobData("templateId", templateId).build();
                    scheduler.rescheduleJob(new TriggerKey(Constant.WATER_QUALITY_TRIGGER_NAME), trigger);
                    scheduler.resumeJob(waterQualityJobKey);
//                    scheduler.start();
                }
                template.setStatus(Constant.UP);
                templateMapper.updateById(template);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SofnException("水质数据获取异常");
            }
        }else {
            //关闭任务调度
            try {
                scheduler.pauseJob(waterQualityJobKey);

            } catch (SchedulerException e) {
                e.printStackTrace();
                throw new SofnException("任务调度停止失败");
            }
            template.setStatus(Constant.OFF);
            templateMapper.updateById(template);
        }
    }

}
