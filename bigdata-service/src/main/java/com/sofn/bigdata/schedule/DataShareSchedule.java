package com.sofn.bigdata.schedule;

import com.sofn.bigdata.job.DataShareJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataShareSchedule {

    public void startDataShare(String cron) throws SchedulerException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(DataShareJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();	//调度对象设置时间并设置需要执行的任务：第一个是对象,第二个是触发的时间
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

}

