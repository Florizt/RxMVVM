package com.rx.mvvm.aop;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.rx.mvvm.BuildConfig;

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
public class ArouterAspect {
    @Pointcut("execution(@com.rx.mvvm.aop.anno.ArouterInit * *(..))")
    public void arouterInit() {

    }

    @Pointcut("execution(@com.rx.mvvm.aop.anno.ArouterDestroy * *(..))")
    public void arouterDestroy() {

    }

    @Around("arouterInit()")
    public void aroundJoinAspectArouterInit(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
        if (joinPoint.getThis() instanceof Application) {
            Application application = (Application) joinPoint.getThis();
            if (BuildConfig.DEBUG) {
                ARouter.openLog();
                ARouter.openDebug();
            }
            ARouter.init(application);
        }
    }

    @Around("arouterDestroy()")
    public void aroundJoinAspectArouterDestroy(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
        ARouter.getInstance().destroy();
    }
}
