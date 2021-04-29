package com.rx.mvvm;

import com.rx.rxmvvmlib.listener.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.listener.ICfgsAdapter;
import com.rx.rxmvvmlib.listener.ICrashHandler;
import com.rx.rxmvvmlib.listener.ICustomHttpCodeFilter;

import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2021/4/28
 * 佛祖保佑       永无BUG
 */
public class DemoCfgsAdapter implements ICfgsAdapter {

    @Override
    public boolean debugEnable() {
        return true;
    }

    @Override
    public int designWidthInDp() {
        return 375;
    }

    @Override
    public int designHeightInDp() {
        return 664;
    }

    @Override
    public ICrashHandler crashHandler() {
        return new CrashHandlerImpl();
    }

    @Override
    public IActivityLifecycleCallbacks activityLifecycleCallbacks() {
        return new ActivityLifecycleCallbacksImpl();
    }

    @Override
    public String floderName() {
        return null;
    }

    @Override
    public String httpBaseUrl() {
        return null;
    }

    @Override
    public String httpSuccessCode() {
        return null;
    }

    @Override
    public List<Interceptor> interceptors() {
        return null;
    }

    @Override
    public ICustomHttpCodeFilter customHttpCodeFilter() {
        return null;
    }
}
