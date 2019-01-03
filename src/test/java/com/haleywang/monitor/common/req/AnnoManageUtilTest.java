package com.haleywang.monitor.common.req;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.haleywang.monitor.utils.AnnotationUtils;

import static org.junit.Assert.*;

public class AnnoManageUtilTest {

    @Test
    public void scan() {
        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestImportAnnotation.class);
        Assert.assertTrue(res.size() > 0);
        for(Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestImportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            Assert.assertNotNull(aName);
        }
    }
}