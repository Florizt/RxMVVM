package com.rx.rxmvvmlib.listener;

import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2021/4/28
 * 佛祖保佑       永无BUG
 */
public interface ICfgsAdapter {

    boolean debugEnable();

    int designWidthInDp();

    int designHeightInDp();

    ICrashHandler crashHandler();

    IActivityLifecycleCallbacks activityLifecycleCallbacks();

    String floderName();

    String httpBaseUrl();

    String httpSuccessCode();

    List<Interceptor> interceptors();

    ICustomHttpCodeFilter customHttpCodeFilter();
}
