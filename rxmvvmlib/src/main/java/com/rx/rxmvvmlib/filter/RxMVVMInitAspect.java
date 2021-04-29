package com.rx.rxmvvmlib.filter;


import android.app.Application;
import android.text.TextUtils;

import com.rx.rxmvvmlib.DefaultCfgsAdapter;
import com.rx.rxmvvmlib.RxMVVMInit;
import com.rx.rxmvvmlib.annotation.RxMVVMInitz;

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
    @Pointcut("execution(@com.rx.rxmvvmlib.annotation.RxMVVMInit * *(..))")
    public void rxMVVMInit() {

    }

    @Around("rxMVVMInit()")
    public void aroundJoinAspectRxMVVMInit(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!(joinPoint.getThis() instanceof Application)) {
            throw new IllegalStateException("RxMVVMInit not in application");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RxMVVMInitz rxMVVMInit = methodSignature.getMethod().getAnnotation(RxMVVMInitz.class);
        if (rxMVVMInit != null
                && !TextUtils.equals(rxMVVMInit.clazz().getName(), DefaultCfgsAdapter.class.getName())) {
            RxMVVMInit.getInstance().init((Application) joinPoint.getThis(), rxMVVMInit.clazz().newInstance());
        } else {
            RxMVVMInit.getInstance().init((Application) joinPoint.getThis());
        }

        joinPoint.proceed();
    }
}
