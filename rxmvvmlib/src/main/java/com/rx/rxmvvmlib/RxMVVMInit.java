package com.rx.rxmvvmlib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.rx.rxmvvmlib.config.AppConfig;
import com.rx.rxmvvmlib.mode.remote.ICustomHttpCodeFilter;
import com.rx.rxmvvmlib.ui.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.ui.ICrashHandler;
import com.rx.rxmvvmlib.ui.base.CrashHandler;
import com.rx.rxmvvmlib.util.LogUtil;
import com.rx.rxmvvmlib.util.UIUtils;
import com.tencent.smtt.sdk.QbSdk;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.Interceptor;


/**
 * Created by wuwei
 * 2019/12/5
 * 佛祖保佑       永无BUG
 */
public class RxMVVMInit {
    private static volatile RxMVVMInit rxMVVMInit;
    public static AppConfig config;

    public static RxMVVMInit getInstance() {
        if (rxMVVMInit == null) {
            synchronized (RxMVVMInit.class) {
                if (rxMVVMInit == null) {
                    rxMVVMInit = new RxMVVMInit();
                }
            }
        }
        return rxMVVMInit;
    }

    static {
        config = new AppConfig();
    }


    /**
     * 初始化
     *
     * @param cx
     */
    public void init(Context cx) {
        init(cx, DefaultCfgsAdapter.create());
    }

    public void init(Context cx, ICfgsAdapter iCfgsAdapter) {
        if (cx == null) {
            throw new IllegalArgumentException("context is null");
        }

        if (iCfgsAdapter == null) {
            iCfgsAdapter = DefaultCfgsAdapter.create();
        }

        buildAdapter(iCfgsAdapter);

        try {
            Context context = cx.getApplicationContext();

            // 主项目配置
            UIUtils.init(context);

            // 崩溃抓取
            CrashHandler.getInstance().init(context, getConfig().getCrashHandler());

            if (getConfig().getActivityLifecycleCallbacks() != null) {
                ((Application) context).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                        getConfig().getActivityLifecycleCallbacks().onActivityCreated(activity, savedInstanceState);
                    }

                    @Override
                    public void onActivityStarted(@NonNull Activity activity) {
                        getConfig().getActivityLifecycleCallbacks().onActivityStarted(activity);
                    }

                    @Override
                    public void onActivityResumed(@NonNull Activity activity) {
                        getConfig().getActivityLifecycleCallbacks().onActivityResumed(activity);
                    }

                    @Override
                    public void onActivityPaused(@NonNull Activity activity) {
                        getConfig().getActivityLifecycleCallbacks().onActivityPaused(activity);
                    }

                    @Override
                    public void onActivityStopped(@NonNull Activity activity) {
                        getConfig().getActivityLifecycleCallbacks().onActivityStopped(activity);
                    }

                    @Override
                    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                        getConfig().getActivityLifecycleCallbacks().onActivitySaveInstanceState(activity, outState);
                    }

                    @Override
                    public void onActivityDestroyed(@NonNull Activity activity) {
                        getConfig().getActivityLifecycleCallbacks().onActivityDestroyed(activity);
                    }
                });
            }

            // 适配配置
            AutoSize.initCompatMultiProcess(context);
            AutoSizeConfig.getInstance().setDesignWidthInDp(getConfig().getDesignWidthInDp());
            AutoSizeConfig.getInstance().setDesignHeightInDp(getConfig().getDesignHeightInDp());

            //腾讯x5内核浏览器配置
            //非wifi情况下，主动下载x5内核
            QbSdk.setDownloadWithoutWifi(true);
            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean arg0) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    LogUtil.i("腾讯x5内核浏览器：-------->  " + arg0);
                }

                @Override
                public void onCoreInitFinished() {

                }
            };
            //x5内核初始化接口
            QbSdk.initX5Environment(context, cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppConfig getConfig() {
        return config;
    }

    private void buildAdapter(ICfgsAdapter iCfgsAdapter) {
        config.setDebugEnable(iCfgsAdapter.debugEnable());
        config.setDesignWidthInDp(iCfgsAdapter.designWidthInDp());
        config.setDesignHeightInDp(iCfgsAdapter.designHeightInDp());
        config.setCrashHandler(iCfgsAdapter.crashHandler());
        config.setActivityLifecycleCallbacks(iCfgsAdapter.activityLifecycleCallbacks());
        config.setFloderName(iCfgsAdapter.floderName());
        config.setHttpBaseUrl(iCfgsAdapter.httpBaseUrl());
        config.setHttpSuccessCode(iCfgsAdapter.httpSuccessCode());
        config.setInterceptors(iCfgsAdapter.interceptors());
        config.setCustomHttpCodeFilter(iCfgsAdapter.customHttpCodeFilter());
    }

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
}
