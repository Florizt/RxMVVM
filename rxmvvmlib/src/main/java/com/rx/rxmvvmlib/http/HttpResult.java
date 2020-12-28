package com.rx.rxmvvmlib.http;

import com.rx.rxmvvmlib.base.BaseEntity;

/**
 * Created by wuwei
 * 2020/12/28
 * 佛祖保佑       永无BUG
 */
public abstract class HttpResult<T> extends BaseEntity {
    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
