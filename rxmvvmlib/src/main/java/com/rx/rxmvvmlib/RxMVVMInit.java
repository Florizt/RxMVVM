package com.rx.rxmvvmlib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.rx.rxmvvmlib.base.CrashHandler;
import com.rx.rxmvvmlib.config.AppConfig;
import com.rx.rxmvvmlib.interfaces.IActivityLifecycleCallbacks;
import com.rx.rxmvvmlib.interfaces.ICrashHandler;
import com.rx.rxmvvmlib.interfaces.ICustomHttpCodeFilter;
import com.rx.rxmvvmlib.util.LogUtil;
import com.rx.rxmvvmlib.util.UIUtils;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.jessyan.autosize.AutoSize;
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
        try {
            Properties properties = new Properties();
            properties.load(RxMVVMInit.class.getResourceAsStream("/assets/rxmvvm.properties"));

            config = new AppConfig();
            config.setDebugEnable(Boolean.parseBoolean(properties.getProperty("debugEnable")));
            config.setDesignWidthInDp(Integer.parseInt(properties.getProperty("designWidthInDp")));
            config.setDesignHeightInDp(Integer.parseInt(properties.getProperty("designHeightInDp")));

            Class<?> crashHandlerClass = Class.forName(properties.getProperty("crashHandlerClass"));
            if (ICrashHandler.class.isAssignableFrom(crashHandlerClass)) {
                config.setCrashHandlerClass((Class<? extends ICrashHandler>) crashHandlerClass);
            }

            Class<?> activityLifecycleCallbacksClass = Class.forName(properties.getProperty("activityLifecycleCallbacksClass"));
            if (IActivityLifecycleCallbacks.class.isAssignableFrom(activityLifecycleCallbacksClass)) {
                config.setActivityLifecycleCallbacksClass((Class<? extends IActivityLifecycleCallbacks>) activityLifecycleCallbacksClass);
            }

            config.setFloderName(properties.getProperty("floderName"));
            config.setHttpDebugUrl(properties.getProperty("httpDebugUrl"));
            config.setHttpReleaseUrl(properties.getProperty("httpReleaseUrl"));
            config.setHttpSuccessCode(properties.getProperty("httpSuccessCode"));

            String interceptorsStr = properties.getProperty("interceptors");
            if (!TextUtils.isEmpty(interceptorsStr)) {
                String[] strings = interceptorsStr.split(",");
                List<Class<? extends Interceptor>> interceptors = new ArrayList<>();
                for (String classPath : strings) {
                    Class<?> interceptor = Class.forName(classPath);
                    if (Interceptor.class.isAssignableFrom(interceptor)) {
                        interceptors.add((Class<? extends Interceptor>) interceptor);
                    }
                }
                config.setInterceptors(interceptors);
            }

            Class<?> customHttpCodeFilterClass = Class.forName(properties.getProperty("customHttpCodeFilterClass"));
            if (ICustomHttpCodeFilter.class.isAssignableFrom(customHttpCodeFilterClass)) {
                config.setCustomHttpCodeFilterClass((Class<? extends ICustomHttpCodeFilter>) customHttpCodeFilterClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @param cx
     */
    public void init(Context cx) {
        if (cx == null) {
            throw new IllegalArgumentException("context is null");
        }

        try {
            Context context = cx.getApplicationContext();

            // 主项目配置
            UIUtils.init(context);

            ARouter.init((Application) context);

            // 崩溃抓取
            if (config.crashHandlerClass != null) {
                CrashHandler.getInstance().init(context, config.crashHandlerClass.newInstance());
            }

            if (config.activityLifecycleCallbacksClass != null) {
                final IActivityLifecycleCallbacks iActivityLifecycleCallbacks = config.activityLifecycleCallbacksClass.newInstance();

                ((Application) context).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                        iActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
                    }

                    @Override
                    public void onActivityStarted(@NonNull Activity activity) {
                        iActivityLifecycleCallbacks.onActivityStarted(activity);
                    }

                    @Override
                    public void onActivityResumed(@NonNull Activity activity) {
                        iActivityLifecycleCallbacks.onActivityResumed(activity);
                    }

                    @Override
                    public void onActivityPaused(@NonNull Activity activity) {
                        iActivityLifecycleCallbacks.onActivityPaused(activity);
                    }

                    @Override
                    public void onActivityStopped(@NonNull Activity activity) {
                        iActivityLifecycleCallbacks.onActivityStopped(activity);
                    }

                    @Override
                    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                        iActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
                    }

                    @Override
                    public void onActivityDestroyed(@NonNull Activity activity) {
                        iActivityLifecycleCallbacks.onActivityDestroyed(activity);
                    }
                });
            }
            // 适配配置
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            appInfo.metaData.putInt("design_width_in_dp", config.designWidthInDp);
            appInfo.metaData.putInt("design_height_in_dp", config.designHeightInDp);
            AutoSize.initCompatMultiProcess(context);

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
}
