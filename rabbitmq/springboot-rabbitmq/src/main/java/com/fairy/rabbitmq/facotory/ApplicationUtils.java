package com.fairy.rabbitmq.facotory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author 鹿少年
 * @date 2022/11/14 21:57
 */
@Configuration
public class ApplicationUtils implements ApplicationContextAware {

    ApplicationContext context ;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public  Object getBean(String beanName){
        return context.getBean(beanName);
    }
}
