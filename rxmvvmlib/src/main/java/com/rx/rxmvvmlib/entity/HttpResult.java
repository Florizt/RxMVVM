package com.rx.rxmvvmlib.entity;


/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public class HttpResult<T> extends BaseEntity {
    /**
     * 1 成功
     */
    private String code;
    /**
     * 封装需要返回的数据
     */
    private T content;
    /**
     * 给用户的提示信息
     */
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "code=" + code +
                ", content=" + content +
                ", message" + message + '\'' +
                '}';
    }
}
