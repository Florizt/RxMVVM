package com.rx.mvvm;

import com.rx.rxmvvmlib.annotation.HttpCode;
import com.rx.rxmvvmlib.annotation.HttpData;
import com.rx.rxmvvmlib.annotation.HttpMsg;

/**
 * Created by wuwei
 * 2020/12/28
 * 佛祖保佑       永无BUG
 */
public class HttpResultImpl<D> {
    @HttpCode
    private String httpCode;
    @HttpMsg
    private String httpMsg;
    @HttpData
    private D content;

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMsg() {
        return httpMsg;
    }

    public void setHttpMsg(String httpMsg) {
        this.httpMsg = httpMsg;
    }

    public D getContent() {
        return content;
    }

    public void setContent(D content) {
        this.content = content;
    }
}