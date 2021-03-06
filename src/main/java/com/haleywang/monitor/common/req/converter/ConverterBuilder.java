package com.haleywang.monitor.common.req.converter;

import com.google.common.base.Preconditions;
import com.haleywang.monitor.common.req.AnnoManageUtil;
import com.haleywang.monitor.common.req.MyRequestExportAnnotation;
import com.haleywang.monitor.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author haley
 * @date 2018/12/16
 */
public class ConverterBuilder {

    public ReqConverter build(String type) {
        Preconditions.checkNotNull(type, "Type should not be null");

        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestExportAnnotation.class);
        for (Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestExportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            if (type.equals(aName)) {
                ReqConverter rc = null;
                try {
                    rc = (ReqConverter) c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return rc;
            }
        }
        return null;

    }

}
