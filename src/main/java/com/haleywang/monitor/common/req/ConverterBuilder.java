package com.haleywang.monitor.common.req;

import java.lang.annotation.Annotation;
import java.util.List;

import com.google.common.base.Preconditions;
import com.haleywang.monitor.utils.AnnotationUtils;

public class ConverterBuilder {

    public ReqConverter build(String type) {
        Preconditions.checkNotNull(type, "Type should not be null");

        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestExportAnnotation.class);
        for(Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestExportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            if(type.equals(aName)) {
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
