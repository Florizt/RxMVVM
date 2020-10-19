package com.rx.rxmvvmlib.entity.http;


import com.rx.rxmvvmlib.entity.BaseEntity;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public class HttpResult<T> extends BaseEntity {
    /**
     * 0 成功
     */
    private String code;
    /**
     * 封装需要返回的数据
     */
    private T data;
    /**
     * 给用户的提示信息
     */
    private String err;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "code=" + code +
                ", data=" + data +
                ", err='" + err + '\'' +
                '}';
    }
}
