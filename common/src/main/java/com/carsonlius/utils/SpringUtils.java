package com.carsonlius.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static ApplicationContext parentApplicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Assert.notNull(applicationContext, "SpringUtil injection ApplicationContext is null");
        SpringUtils.applicationContext = applicationContext;
        parentApplicationContext = applicationContext.getParent();
    }

    public  Object getBean(String name) {
        Assert.hasText(name, "SpringUtil name is null or empty");
        try {
            return applicationContext.getBean(name);
        } catch (Exception e) {
            return parentApplicationContext.getBean(name);
        }
    }

    public  <T> T getBean(String name, Class<T> type) {
        Assert.hasText(name, "SpringUtil name is null or empty");
        Assert.notNull(type, "SpringUtil type is null");
        try {
            return applicationContext.getBean(name, type);
        } catch (Exception e) {
            return parentApplicationContext.getBean(name, type);
        }
    }

    public  <T> T getBean(Class<T> type) {
        Assert.notNull(type, "SpringUtil type is null");
        try {
            return applicationContext.getBean(type);
        } catch (Exception e) {
            return parentApplicationContext.getBean(type);
        }
    }

    public  <T> Map<String, T> getBeansOfType(Class<T> type) {
        Assert.notNull(type, "SpringUtil type is null");
        try {
            return applicationContext.getBeansOfType(type);
        } catch (Exception e) {
            return parentApplicationContext.getBeansOfType(type);
        }
    }
}
