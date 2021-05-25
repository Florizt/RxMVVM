package com.rx.mvvm.aop;


import android.app.Application;

import com.rx.mvvm.aop.anno.DBInit;
import com.rx.mvvm.repository.greendao.DaoManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by wuwei
 * 2021/3/22
 * 佛祖保佑       永无BUG
 */
@Aspect
public class DBInitAspect {
    @Pointcut("execution(@com.rx.mvvm.aop.anno.DBInit * *(..))")
    public void dBInit() {

    }

    @Around("dBInit()")
    public void aroundJoinAspectDBInit(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!(joinPoint.getThis() instanceof Application)) {
            throw new IllegalStateException("DBInit not in application");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DBInit dbInit = methodSignature.getMethod().getAnnotation(DBInit.class);
        if (dbInit != null) {
            DaoManager.getInstance().init(((Application) joinPoint.getThis()).getApplicationContext(),
                    dbInit.dbName(), dbInit.daoClasses());
        }

        joinPoint.proceed();
    }
}
