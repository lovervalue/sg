package com.sofn.bigdata.config;

import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Configuration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @author:袁彬锋
 * @date:2020/5/28 16:45
 * @company:曙光集团绿康元公司
 * @description:
 */
@Configuration
public class QuartzServletContextListener extends QuartzInitializerListener {
    public static final String MY_CONTEXT_NAME = "servletContext";

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TO DO Auto-generated method stub
        super.contextDestroyed(sce);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // TO DO Auto-generated method stub
        super.contextInitialized(sce);
        ServletContext servletContext = sce.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) servletContext
                .getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
        try {
            factory.getScheduler().getContext().put(QuartzServletContextListener.MY_CONTEXT_NAME, servletContext);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
