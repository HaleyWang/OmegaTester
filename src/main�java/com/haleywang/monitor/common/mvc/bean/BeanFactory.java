package com.haleywang.monitor.common.mvc.bean;

/**
 * @author haley
 */
public interface BeanFactory {

    /**
     * Get bean by type
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> type);

}
