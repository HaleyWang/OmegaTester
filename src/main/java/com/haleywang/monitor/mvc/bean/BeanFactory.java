package com.haleywang.monitor.mvc.bean;

public interface BeanFactory {

    <T> T getBean(Class<T> type);

}
