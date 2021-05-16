package com.rx.rxmvvmlib.aop.filter.reexecute;

import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;

/**
 * Created by wuwei
 * 2021/4/26
 * 佛祖保佑       永无BUG
 */
public class ReExecuteEntity implements Serializable {
    private MethodSignature methodSignature;
    private Object object;
    private Object[] args;

    public ReExecuteEntity() {

    }

    public ReExecuteEntity(MethodSignature methodSignature, Object object, Object[] args) {
        this.methodSignature = methodSignature;
        this.object = object;
        this.args = args;
    }

    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(MethodSignature methodSignature) {
        this.methodSignature = methodSignature;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
