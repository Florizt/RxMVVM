package com.rx.rxmvvmlib.repository.config;

import android.content.Context;

import com.rx.rxmvvmlib.BuildConfig;
import com.rx.rxmvvmlib.RMEngine;
import com.rx.rxmvvmlib.repository.datasource.remote.ICustomHttpCodeFilter;
import com.rx.rxmvvmlib.ui.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.ui.ICrashHandler;
import com.rx.rxmvvmlib.util.LogUtil;

import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2021/4/27
 * 佛祖保佑       永无BUG
 */
public class DefaultCfgsAdapter implements RMEngine.ICfgsAdapter {

    public static DefaultCfgsAdapter create() {
        return new DefaultCfgsAdapter();
    }

    @Override
    public boolean debugEnable() {
        return BuildConfig.DEBUG;
    }

    @Override
    public int designWidthInDp() {
        return 360;
    }

    @Override
    public int designHeightInDp() {
        return 640;
    }

    @Override
    public ICrashHandler crashHandler() {
        return new ICrashHandler() {
            @Override
            public void reportError(Context context, Throwable ex) {
                LogUtil.e(ex);
            }
        };
    }

    @Override
    public IActivityLifecycleCallbacks activityLifecycleCallbacks() {
        return new IActivityLifecycleCallbacks() {
            @Override
            public void back2App(Context context) {
                LogUtil.e("back2App");
            }

            @Override
            public void leaveApp(Context context) {
                LogUtil.e("leaveApp");
            }
        };
    }

    @Override
    public String floderName() {
        return "rxmvvm";
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
