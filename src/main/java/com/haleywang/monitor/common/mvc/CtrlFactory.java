package com.haleywang.monitor.common.mvc;

import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.ctrl.v1.ReqCtrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haley on 2018/8/17.
 */
public class CtrlFactory {

    private CtrlFactory(){}

    private static Map<String, Class> nameClassMap = new HashMap<>();
    static {
        nameClassMap.put("ReqCtrl", ReqCtrl.class);
    }


    public static BaseCtrl of(String klassName) {

        String className = klassName.substring(0, 1).toUpperCase() + klassName.substring(1) + "Ctrl";
        try {
            //todo cache

            Class cls = nameClassMap.get(className);

            if(cls == null) {
                cls = Class.forName("com.haleywang.monitor.ctrl.v1." + className);
            }

            return (BaseCtrl) cls.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new ReqException(e.getMessage(), e);

        }

    }




}
