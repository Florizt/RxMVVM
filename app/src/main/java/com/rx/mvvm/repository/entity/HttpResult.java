package com.rx.mvvm.repository.entity;

import com.rx.rxmvvmlib.repository.datasource.remote.retrofit.anno.HttpCode;
import com.rx.rxmvvmlib.repository.datasource.remote.retrofit.anno.HttpData;
import com.rx.rxmvvmlib.repository.datasource.remote.retrofit.anno.HttpMsg;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class HttpResult<D> {
    @HttpCode
    private int code;
    @HttpMsg
    private String msg;
    @HttpData
    private D data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}


