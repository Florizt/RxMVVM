package com.rx.mvvm;

import android.content.Context;

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
public class AppCfgsAdapter implements RxMVVMInit.ICfgsAdapter {

    @Override
    public boolean debugEnable() {
        return BuildConfig.DEBUG;
    }

    @Override
    public int designWidthInDp() {
        return 375;
    }

    @Override
    public int designHeightInDp() {
        return 667;
    }

    @Override
    public ICrashHandler crashHandler() {
        return new ICrashHandler() {
            @Override
            public void reportError(Context context, Throwable ex) {

            }
        };
    }

    @Override
    public IActivityLifecycleCallbacks activityLifecycleCallbacks() {
        return new IActivityLifecycleCallbacks() {
            @Override
            public void back2App(Context context) {

            }

            @Override
            public void leaveApp(Context context) {

            }
        };
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
