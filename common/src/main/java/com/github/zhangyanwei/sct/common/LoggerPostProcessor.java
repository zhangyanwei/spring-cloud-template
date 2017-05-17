package com.github.zhangyanwei.sct.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.util.ReflectionUtils.*;

@Component
public class LoggerPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Logger logger = LoggerFactory.getLogger(bean.getClass());
        doWithFields(bean.getClass(), field -> {
            makeAccessible(field);
            setField(field, bean, logger);
        }, field -> field.isAnnotationPresent(Autowired.class) && Logger.class.equals(field.getType()));

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object entity, String beanName) throws BeansException {
        return entity;
    }
}