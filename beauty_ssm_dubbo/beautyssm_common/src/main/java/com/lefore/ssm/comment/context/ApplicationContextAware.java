package com.lefore.ssm.comment.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 描述：获取 Spring 上下文
 * <p>
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
@Lazy(value = false)
@Component
public class ApplicationContextAware implements org.springframework.context.ApplicationContextAware {

    public static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextAware.ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
}
