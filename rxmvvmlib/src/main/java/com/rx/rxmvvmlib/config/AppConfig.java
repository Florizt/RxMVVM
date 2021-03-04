package com.rx.rxmvvmlib.config;

import com.rx.rxmvvmlib.interfaces.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.interfaces.ICrashHandler;
import com.rx.rxmvvmlib.interfaces.ICustomHttpCodeFilter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2020/10/19
 * 佛祖保佑       永无BUG
 */
public class AppConfig {
    //ui
    public boolean debugEnable;
    public int designWidthInDp;
    public int designHeightInDp;
    public Class<? extends ICrashHandler> crashHandlerClass;
    public Class<? extends IActivityLifecycleCallbacks> activityLifecycleCallbacksClass;
    public String floderName;
    //network
    public String httpDebugUrl;
    public String httpReleaseUrl;
    public String httpSuccessCode;
    public List<Class<? extends Interceptor>> interceptors;
    public Class<? extends ICustomHttpCodeFilter> customHttpCodeFilterClass;

    public void setDebugEnable(boolean debugEnable) {
        this.debugEnable = debugEnable;
    }

    public void setDesignWidthInDp(int designWidthInDp) {
        this.designWidthInDp = designWidthInDp;
    }

    public void setDesignHeightInDp(int designHeightInDp) {
        this.designHeightInDp = designHeightInDp;
    }

    public void setCrashHandlerClass(Class<? extends ICrashHandler> crashHandlerClass) {
        this.crashHandlerClass = crashHandlerClass;
    }

    public void setActivityLifecycleCallbacksClass(Class<? extends IActivityLifecycleCallbacks> activityLifecycleCallbacksClass) {
        this.activityLifecycleCallbacksClass = activityLifecycleCallbacksClass;
    }

    public void setFloderName(String floderName) {
        this.floderName = floderName;
    }

    public void setHttpDebugUrl(String httpDebugUrl) {
        this.httpDebugUrl = httpDebugUrl;
    }

    public void setHttpReleaseUrl(String httpReleaseUrl) {
        this.httpReleaseUrl = httpReleaseUrl;
    }

    public void setHttpSuccessCode(String httpSuccessCode) {
        this.httpSuccessCode = httpSuccessCode;
    }

    public void setInterceptors(List<Class<? extends Interceptor>> interceptors) {
        if (this.interceptors == null) {
            this.interceptors = new ArrayList<>();
        }
        this.interceptors.addAll(interceptors);
    }

    public void setCustomHttpCodeFilterClass(Class<? extends ICustomHttpCodeFilter> customHttpCodeFilterClass) {
        this.customHttpCodeFilterClass = customHttpCodeFilterClass;
    }
}
