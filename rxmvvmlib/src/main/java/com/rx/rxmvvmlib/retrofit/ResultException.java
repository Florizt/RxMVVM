package com.rx.rxmvvmlib.retrofit;

public class ResultException extends RuntimeException {

    private String errMsg;
    private String errCode;

    public ResultException() {

    }

    public ResultException(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
