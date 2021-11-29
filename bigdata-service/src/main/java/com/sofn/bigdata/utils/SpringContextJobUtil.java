package com.sofn.bigdata.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @Acthor Tao.Lee @date 2021/1/29 14:04
 * @Description
 */
@Component
public class SpringContextJobUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    @SuppressWarnings("static-access" )
    public void setApplicationContext(ApplicationContext contex)
            throws BeansException {
        // TODO Auto-generated method stub
        this.context = contex;
    }
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }
}