package com.haleywang.monitor.common;

import com.google.common.collect.ImmutableList;
import com.haleywang.monitor.common.req.AnnoManageUtil;
import com.haleywang.monitor.common.req.MyRequestExportAnnotation;
import com.haleywang.monitor.common.req.MyRequestImportAnnotation;
import com.haleywang.monitor.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class Constants {

    private Constants(){}

    public static final String LOGIN_COOKIE = "hmsso";
    public static final String CURRENT_ACCOUNT = "current_account";
    public static final String REQUEST_ID_IN_LOG = "_req_id_in_log";

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static final List<String> EXPORT_TYPE;

    static {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestExportAnnotation.class);
        for (Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestExportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            Optional.ofNullable(aName).ifPresent(builder::add);
        }

        EXPORT_TYPE = builder.build();
    }


    public static final List<String> IMPORT_TYPE;

    static {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        String pName = "com.haleywang.monitor.common.req";
        List<Class<?>> res = AnnoManageUtil.scan(pName, MyRequestImportAnnotation.class);
        for (Class c : res) {
            Annotation ann = AnnotationUtils.findAnnotation(c, MyRequestImportAnnotation.class);
            String aName = (String) AnnotationUtils.getValue(ann, "name");
            Optional.ofNullable(aName).ifPresent(builder::add);
        }
        IMPORT_TYPE = builder.build();
    }
}
