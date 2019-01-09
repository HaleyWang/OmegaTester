package com.haleywang.monitor.common.mvc;

import java.lang.annotation.*;


@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamBody {

        String name() default "";
}
