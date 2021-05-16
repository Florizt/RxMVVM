package com.rx.rxmvvmlib;

import com.rx.rxmvvmlib.view.ICrashHandler;
import com.rx.rxmvvmlib.mode.remote.ICustomHttpCodeFilter;
import com.rx.rxmvvmlib.view.IActivityLifecycleCallbacks;

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
