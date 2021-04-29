package com.rx.rxmvvmlib.filter;


import com.rx.rxmvvmlib.filter.reexecute.ReExecuteCache;
import com.rx.rxmvvmlib.filter.reexecute.ReExecuteEntity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by wuwei
 * 2021/3/22
 * 佛祖保佑       永无BUG
 */
@Aspect
public class LoginReExecuteAspect {
    @Pointcut("execution(@com.rx.rxmvvmlib.annotation.LoginReExecute * *(..))")
    public void loginReExecute() {

    }

    @Around("loginReExecute()")
    public void aroundJoinAspectLoginReExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            joinPoint.proceed();

            ReExecuteEntity entity = ReExecuteCache.getCache();
            if (entity != null) {
                try {
                    entity.getMethodSignature().getMethod().invoke(entity.getObject(), entity.getArgs());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ReExecuteCache.clearCache();
        } catch (Exception e) {
            e.printStackTrace();
            joinPoint.proceed();
            ReExecuteCache.clearCache();
        }
    }
}
