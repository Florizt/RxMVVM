package com.rx.rxmvvmlib.entity.http;


import com.rx.rxmvvmlib.entity.BaseEntity;

/**
 * Created by wuwei
 * 2018/1/18
 * 佛祖保佑       永无BUG
 */
public class HttpErrResult extends BaseEntity {
    /**
     * 0 成功
     */
    private int code;
    /**
     * 给用户的提示信息
     */
    private String err;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
                ", err='" + err + '\'' +
                '}';
    }
}
