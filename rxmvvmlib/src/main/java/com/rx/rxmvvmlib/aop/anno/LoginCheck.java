package com.rx.rxmvvmlib.aop.anno;

import com.rx.rxmvvmlib.aop.ILoginCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuwei
 * 2021/3/22
 * 佛祖保佑       永无BUG
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCheck {

    Class<? extends ILoginCheck> clazz();

    boolean reExecute() default false;//登录完成是否继续执行上一次操作
}
