package com.haleywang.monitor.mvc;

import com.haleywang.monitor.common.ReqException;

/**
 * Created by haley on 2018/8/17.
 */
public class CtrlFactory {


    public static BaseCtrl of(String klassName) {

        String className = klassName.substring(0, 1).toUpperCase() + klassName.substring(1);
        try {
            //todo cache
            Class<?> cls = Class.forName("com.haleywang.monitor.ctrl.v1."+ className + "Ctrl");
            return (BaseCtrl) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ReqException(e.getMessage(), e);

        } catch (InstantiationException e) {
            throw new ReqException(e.getMessage(), e);

        } catch (IllegalAccessException e) {
            throw new ReqException(e.getMessage(), e);
        }

    }




}
