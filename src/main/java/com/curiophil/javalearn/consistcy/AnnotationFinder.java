package com.curiophil.javalearn.consistcy;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public class AnnotationFinder {

    public static Method findRollbackMethod(String className, String annotationValue) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        AtomicReference<Method> result = new AtomicReference<>();
        ReflectionUtils.doWithMethods(clazz, method -> {
            Rollback execute = method.getAnnotation(Rollback.class);
            if (execute != null && annotationValue.equals(execute.value())) {
                result.set(method);
            }
        });
        return result.get();
    }

}