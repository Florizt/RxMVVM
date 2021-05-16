package com.rx.rxmvvmlib.config;

import com.rx.rxmvvmlib.view.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.view.ICrashHandler;
import com.rx.rxmvvmlib.mode.remote.ICustomHttpCodeFilter;

import java.io.Serializable;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2021/4/28
 * 佛祖保佑       永无BUG
 */
public class AppConfig implements Serializable {
    //ui
    private boolean debugEnable;
    private int designWidthInDp;
    private int designHeightInDp;
    private ICrashHandler crashHandler;
    private IActivityLifecycleCallbacks activityLifecycleCallbacks;
    private String floderName;
    //network
    private String httpBaseUrl;
    private String httpSuccessCode;
    private List<Interceptor> interceptors;
    private ICustomHttpCodeFilter customHttpCodeFilter;

    public boolean isDebugEnable() {
        return debugEnable;
    }

    public void setDebugEnable(boolean debugEnable) {
        this.debugEnable = debugEnable;
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

    public ICrashHandler getCrashHandler() {
        return crashHandler;
    }

    public void setCrashHandler(ICrashHandler crashHandler) {
        this.crashHandler = crashHandler;
    }

    public IActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return activityLifecycleCallbacks;
    }

    public void setActivityLifecycleCallbacks(IActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
    }

    public String getFloderName() {
        return floderName;
    }

    public void setFloderName(String floderName) {
        this.floderName = floderName;
    }

    public String getHttpBaseUrl() {
        return httpBaseUrl;
    }

    public void setHttpBaseUrl(String httpBaseUrl) {
        this.httpBaseUrl = httpBaseUrl;
    }

    public String getHttpSuccessCode() {
        return httpSuccessCode;
    }

    public void setHttpSuccessCode(String httpSuccessCode) {
        this.httpSuccessCode = httpSuccessCode;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public ICustomHttpCodeFilter getCustomHttpCodeFilter() {
        return customHttpCodeFilter;
    }

    public void setCustomHttpCodeFilter(ICustomHttpCodeFilter customHttpCodeFilter) {
        this.customHttpCodeFilter = customHttpCodeFilter;
    }
}
