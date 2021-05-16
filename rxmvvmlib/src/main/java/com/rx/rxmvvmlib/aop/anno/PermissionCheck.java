package com.rx.rxmvvmlib.aop.anno;

import com.rx.rxmvvmlib.aop.IPermissionCheckDenine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuwei
 * 2021/3/25
 * 佛祖保佑       永无BUG
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCheck {
    String[] permissions() default {};

    Class<? extends IPermissionCheckDenine> clazz();
}
