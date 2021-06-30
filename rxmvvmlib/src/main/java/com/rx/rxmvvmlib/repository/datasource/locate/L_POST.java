package com.rx.rxmvvmlib.repository.datasource.locate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuwei
 * 2021/5/24
 * 佛祖保佑       永无BUG
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface L_POST {
    LocalType type() default LocalType.SP;

    String[] key() default "";//for sp key-value
}
