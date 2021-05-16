package com.rx.rxmvvmlib.aop.anno;

import com.rx.rxmvvmlib.DefaultCfgsAdapter;
import com.rx.rxmvvmlib.ICfgsAdapter;

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
public @interface RxMVVMInitz {
    Class<? extends ICfgsAdapter> clazz() default DefaultCfgsAdapter.class;
}
