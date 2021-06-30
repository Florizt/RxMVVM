package com.rx.rxmvvmlib.aop.filter;


import android.app.Application;
import android.text.TextUtils;

import com.rx.rxmvvmlib.RMEngine;
import com.rx.rxmvvmlib.aop.anno.RxMVVMInit;
import com.rx.rxmvvmlib.repository.config.DefaultCfgsAdapter;

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
public class RxMVVMInitAspect {
    @Pointcut("execution(@com.rx.rxmvvmlib.aop.anno.RxMVVMInit * *(..))")
    private void rxMVVMInit() {

    }

    @Around("rxMVVMInit()")
    public void aroundJoinAspectRxMVVMInit(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();

        if (!(joinPoint.getThis() instanceof Application)) {
            throw new IllegalStateException("RxMVVMInit not in application");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RxMVVMInit rxMVVMInit = methodSignature.getMethod().getAnnotation(RxMVVMInit.class);
        if (rxMVVMInit != null
                && !TextUtils.equals(rxMVVMInit.clazz().getName(), DefaultCfgsAdapter.class.getName())) {
            RMEngine.getInstance().init((Application) joinPoint.getThis(), rxMVVMInit.clazz().newInstance());
        } else {
            RMEngine.getInstance().init((Application) joinPoint.getThis());
        }
    }
}
