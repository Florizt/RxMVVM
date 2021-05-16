package com.rx.rxmvvmlib;

import android.content.Context;

import com.rx.rxmvvmlib.view.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.view.ICrashHandler;
import com.rx.rxmvvmlib.mode.remote.ICustomHttpCodeFilter;
import com.rx.rxmvvmlib.util.LogUtil;

import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wuwei
 * 2021/4/27
 * 佛祖保佑       永无BUG
 */
public class DefaultCfgsAdapter implements ICfgsAdapter {

    public static DefaultCfgsAdapter create() {
        return new DefaultCfgsAdapter();
    }

    @Override
    public boolean debugEnable() {
        return true;
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
