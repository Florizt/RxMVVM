package com.rx.rxmvvmlib.aop.filter;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.rx.rxmvvmlib.aop.IPermissionCheckDenine;
import com.rx.rxmvvmlib.aop.anno.PermissionCheck;
import com.rx.rxmvvmlib.ui.base.AppManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import io.reactivex.functions.Consumer;

/**
 * Created by wuwei
 * 2021/3/22
 * 佛祖保佑       永无BUG
 */
@Aspect
public class PermissionAspect {
    @Pointcut("execution(@com.rx.rxmvvmlib.aop.anno.PermissionCheck * *(..))")
    private void permissionCheck() {

    }

    @SuppressLint("CheckResult")
    @Around("permissionCheck()")
    public void aroundJoinAspectPermissionCheck(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final PermissionCheck permissionCheck = methodSignature.getMethod().getAnnotation(PermissionCheck.class);

        if (permissionCheck != null && permissionCheck.permissions().length > 0) {
            try {
                final Activity activity = AppManager.getInstance().currentActivity();
                if (null != activity) {
                    RxPermissions rxPermissions = new RxPermissions(activity);
                    rxPermissions
                            .request(permissionCheck.permissions())
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean b) throws Exception {
                                    if (b) {
                                        try {
                                            joinPoint.proceed();
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    } else {
                                        IPermissionCheckDenine iPermissionCheckDenine = permissionCheck.clazz().newInstance();
                                        iPermissionCheckDenine.permissionCheckDenine(activity);
                                    }
                                }
                            });
                } else {
                    joinPoint.proceed();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            joinPoint.proceed();
        }
    }
}
