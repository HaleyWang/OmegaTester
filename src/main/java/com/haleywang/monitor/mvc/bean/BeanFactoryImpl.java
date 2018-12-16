package com.haleywang.monitor.mvc.bean;

import com.haleywang.monitor.mvc.OakException;

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
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new OakException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new OakException(e.getMessage(), e);
        }
    }
}
