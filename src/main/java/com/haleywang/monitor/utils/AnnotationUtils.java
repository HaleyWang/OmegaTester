package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AnnotationUtils {

    static final String VALUE = "value";

    public static Annotation[] getAnnotations(Method method) {

        return method.getAnnotations();
    }

    public static Annotation getAnnotation(Method method, Class annotationType) {
        return method.getAnnotation(annotationType);
    }


    public static Annotation findAnnotation(Method method, Class annotationType) {
        Annotation annotation = getAnnotation(method, annotationType);
        Class cl = method.getDeclaringClass();
        do {
            if (annotation != null) {
                break;
            }
            cl = cl.getSuperclass();
            if (cl == null || cl.equals(Object.class)) {
                break;
            }
            try {
                Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
                annotation = getAnnotation(equivalentMethod, annotationType);
            } catch (NoSuchMethodException ex) {
                log.error("findAnnotation error", ex);
            }
        } while (true);
        return annotation;
    }


    public static Annotation findAnnotation(Class clazz, Class annotationType) {
        Annotation annotation = clazz.getAnnotation(annotationType);
        if (annotation != null)
            return annotation;
        Class clazzes[] = clazz.getInterfaces();
        int len = clazzes.length;
        for (int i = 0; i < len; i++) {
            Class ifc = clazzes[i];
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null)
                return annotation;
        }

        if (clazz.getSuperclass() == null || Object.class.equals(clazz.getSuperclass()))
            return null;
        else
            return findAnnotation(clazz.getSuperclass(), annotationType);
    }


    public static Class findAnnotationDeclaringClass(Class annotationType, Class clazz) {
        if (clazz == null || clazz.equals(Object.class))
            return null;
        else
            return isAnnotationDeclaredLocally(annotationType, clazz) ? clazz : findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
    }


    public static boolean isAnnotationDeclaredLocally(Class annotationType, Class clazz) {
        boolean declaredLocally = false;
        Iterator iterator = Arrays.asList(clazz.getDeclaredAnnotations()).iterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            Annotation annotation = (Annotation) iterator.next();
            if (!annotation.annotationType().equals(annotationType)) {
                continue;
            }
            declaredLocally = true;
            break;
        } while (true);
        return declaredLocally;
    }

    public static boolean isAnnotationInherited(Class annotationType, Class clazz) {
        return clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz);
    }


    public static Map getAnnotationAttributes(Annotation annotation) {
        Map attrs = new HashMap();
        Method methods[] = annotation.annotationType().getDeclaredMethods();
        for (int j = 0; j < methods.length; j++) {
            Method method = methods[j];
            if (method.getParameterTypes().length != 0 || method.getReturnType() == Void.TYPE)
                continue;
            try {
                attrs.put(method.getName(), method.invoke(annotation, new Object[0]));
            } catch (Exception ex) {
                throw new IllegalStateException("Could not obtain annotation attribute values", ex);
            }
        }

        return attrs;
    }

    public static Object getValue(Annotation annotation) {
        return getValue(annotation, VALUE);
    }


    public static Object getValue(Annotation annotation, String attributeName) {
        try {
            Object[] args = new Object[0];
            Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
            return method.invoke(annotation, args);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Object getDefaultValue(Annotation annotation) {
        return getDefaultValue(annotation, VALUE);
    }

    public static Object getDefaultValue(Annotation annotation, String attributeName) {
        return getDefaultValue(annotation.annotationType(), attributeName);
    }

    public static Object getDefaultValue(Class annotationType) {
        return getDefaultValue(annotationType, VALUE);
    }

    public static Object getDefaultValue(Class annotationType, String attributeName) {
        try {
            Method method = annotationType.getDeclaredMethod(attributeName, new Class[0]);
            return method.getDefaultValue();
        } catch (Exception ex) {
            return null;
        }
    }

}