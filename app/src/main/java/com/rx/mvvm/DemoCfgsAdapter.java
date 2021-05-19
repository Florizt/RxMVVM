package com.rx.mvvm;

import com.rx.rxmvvmlib.RxMVVMInit;
import com.rx.rxmvvmlib.mode.remote.ICustomHttpCodeFilter;
import com.rx.rxmvvmlib.ui.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.ui.ICrashHandler;

import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2021/4/28
 * 佛祖保佑       永无BUG
 */
public class DemoCfgsAdapter implements RxMVVMInit.ICfgsAdapter {

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
        return "test";
    }

    @Override
    public String httpBaseUrl() {
        return "https://www.baidu.com";
    }

    @Override
    public String httpSuccessCode() {
        return "1";
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
