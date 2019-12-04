package com.forgqi.resourcebaseserver.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public ApplicationContextHelper() {
    }

    public static Object getBean(String beanName) {
        return applicationContext != null ? applicationContext.getBean(beanName) : null;
    }

    public static <T> T getBean(Class<T> beanClz) {
        return applicationContext != null ? applicationContext.getBean(beanClz) : null;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }
}
