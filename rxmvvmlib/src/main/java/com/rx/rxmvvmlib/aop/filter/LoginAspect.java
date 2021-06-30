package com.rx.rxmvvmlib.aop.filter;


import com.rx.rxmvvmlib.aop.ILoginCheck;
import com.rx.rxmvvmlib.aop.anno.LoginCheck;
import com.rx.rxmvvmlib.aop.filter.reexecute.ReExecuteCache;
import com.rx.rxmvvmlib.aop.filter.reexecute.ReExecuteEntity;

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
public class LoginAspect {
    @Pointcut("execution(@com.rx.rxmvvmlib.aop.anno.LoginCheck * *(..))")
    private void loginCheck() {

    }

    @Around("loginCheck()")
    public void aroundJoinAspectLoginCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            ReExecuteCache.clearCache();

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            LoginCheck loginCheck = methodSignature.getMethod().getAnnotation(LoginCheck.class);
            ILoginCheck iLoginCheck = loginCheck.clazz().newInstance();
            if (!iLoginCheck.isLogin()) {
                ReExecuteEntity reExecuteEntity = null;
                if (loginCheck.reExecute()) {
                    reExecuteEntity = new ReExecuteEntity();
                    reExecuteEntity.setMethodSignature(methodSignature);
                    reExecuteEntity.setObject(joinPoint.getThis());
                    reExecuteEntity.setArgs(joinPoint.getArgs());

                    ReExecuteCache.addCache(reExecuteEntity);
                }

                //启动登录
                iLoginCheck.toLogin();
                return;
            }
            joinPoint.proceed();
        } catch (Exception e) {
            e.printStackTrace();
            joinPoint.proceed();
            ReExecuteCache.clearCache();
        }
    }
}
