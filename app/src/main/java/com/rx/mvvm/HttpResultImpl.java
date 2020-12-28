package com.rx.mvvm;

import com.rx.rxmvvmlib.http.HttpResult;

/**
 * Created by wuwei
 * 2020/12/28
 * 佛祖保佑       永无BUG
 */
public class HttpResultImpl<D> extends HttpResult {
    private String httpCode;
    private String httpMsg;
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
