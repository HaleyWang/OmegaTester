package com.haleywang.monitor.common.mvc.bean;

public interface BeanFactory {

    <T> T getBean(Class<T> type);

}
