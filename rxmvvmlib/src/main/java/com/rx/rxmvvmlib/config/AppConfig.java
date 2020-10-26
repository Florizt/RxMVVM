package com.rx.rxmvvmlib.config;

import com.rx.rxmvvmlib.listener.ICrashHandler;

import java.util.Map;

/**
 * Created by wuwei
 * 2020/10/19
 * 佛祖保佑       永无BUG
 */
public class AppConfig {
    //崩溃抓取配置
    private ICrashHandler crashHandler;
    //适配配置
    private int designWidthInDp = 360;
    private int designHeightInDp = 640;
    //http配置
    private String httpHostName;
    private String httpDebugUrl;
    private String httpReleaseUrl;
    private int cookieNetworkTime = 60 * 1000;
    private int cookieNoNetworkTime = 30 * 24 * 60 * 60 * 1000;
    private String httpSuccessCode;
    private Map<String, String> header;

    public ICrashHandler getCrashHandler() {
        return crashHandler;
    }

    public void setCrashHandler(ICrashHandler crashHandler) {
        this.crashHandler = crashHandler;
    }

    public int getDesignWidthInDp() {
        return designWidthInDp;
    }

    public void setDesignWidthInDp(int designWidthInDp) {
        this.designWidthInDp = designWidthInDp;
    }

    public int getDesignHeightInDp() {
        return designHeightInDp;
    }

    public void setDesignHeightInDp(int designHeightInDp) {
        this.designHeightInDp = designHeightInDp;
    }

    public String getHttpDebugUrl() {
        return httpDebugUrl;
    }

    public void setHttpDebugUrl(String httpDebugUrl) {
        this.httpDebugUrl = httpDebugUrl;
    }

    public String getHttpReleaseUrl() {
        return httpReleaseUrl;
    }

    public void setHttpReleaseUrl(String httpReleaseUrl) {
        this.httpReleaseUrl = httpReleaseUrl;
    }

    public int getCookieNetworkTime() {
        return cookieNetworkTime;
    }

    public void setCookieNetworkTime(int cookieNetworkTime) {
        this.cookieNetworkTime = cookieNetworkTime;
    }

    public int getCookieNoNetworkTime() {
        return cookieNoNetworkTime;
    }

    public void setCookieNoNetworkTime(int cookieNoNetworkTime) {
        this.cookieNoNetworkTime = cookieNoNetworkTime;
    }

    public String getHttpHostName() {
        return httpHostName;
    }

    public void setHttpHostName(String httpHostName) {
        this.httpHostName = httpHostName;
    }

    public String getHttpSuccessCode() {
        return httpSuccessCode;
    }

    public void setHttpSuccessCode(String httpSuccessCode) {
        this.httpSuccessCode = httpSuccessCode;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}
