package com.haleywang.monitor.common.mvc.bean;

import com.haleywang.monitor.common.mvc.BeanException;

/**
 * Created by haley on 2018/11/13.
 */
public class BeanFactoryImpl implements BeanFactory {

    private static class SingletonHolder {
        private static final BeanFactoryImpl INSTANCE = new BeanFactoryImpl();
    }
    private BeanFactoryImpl (){}
    public static final BeanFactoryImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public <T> T getBean(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanException(e.getMessage(), e);
        }
    }
}
