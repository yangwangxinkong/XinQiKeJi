package com.xss.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author zzl
 * @since on 2018/5/10.
 */
public interface AspectApi {
     Object doHandlerAspect(Object[] obj, ProceedingJoinPoint pjp, Method method, boolean isAll)throws Throwable;
}
