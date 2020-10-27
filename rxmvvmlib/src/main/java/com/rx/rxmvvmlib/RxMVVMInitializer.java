package com.rx.rxmvvmlib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.meituan.android.walle.WalleChannelReader;
import com.rx.rxmvvmlib.base.CrashHandler;
import com.rx.rxmvvmlib.config.AppConfig;
import com.rx.rxmvvmlib.util.UIUtils;

import me.jessyan.autosize.AutoSize;


/**
 * Created by wuwei
 * 2019/12/5
 * 佛祖保佑       永无BUG
 */
public class RxMVVMInitializer {
    private static volatile RxMVVMInitializer rxMVVMInitializer;

    public static RxMVVMInitializer getInstance() {
        if (rxMVVMInitializer == null) {
            synchronized (RxMVVMInitializer.class) {
                if (rxMVVMInitializer == null) {
                    rxMVVMInitializer = new RxMVVMInitializer();
                }
            }
        }
        return rxMVVMInitializer;
    }

    private Context context;
    private AppConfig appConfig;

    /**
     * 初始化
     *
     * @param context
     * @param appConfig
     */
    public void init(Context context, AppConfig appConfig) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

        this.context = context.getApplicationContext();
        // 主项目配置
        UIUtils.init(this.context);

        if (appConfig == null) {
            throw new IllegalArgumentException("appConfig is null");
        }
        if (appConfig.getDesignWidthInDp() == 0 || appConfig.getDesignHeightInDp() == 0) {
            throw new IllegalArgumentException("design_width_in_dp or design_height_in_dp is 0");
        }
        if (TextUtils.isEmpty(appConfig.getHttpDebugUrl()) || TextUtils.isEmpty(appConfig.getHttpReleaseUrl())) {
            throw new IllegalArgumentException("httpDebugUrl or httpReleaseUrl is null");
        }

        this.appConfig = appConfig;

        // 崩溃抓取
        CrashHandler.getInstance().init(this.context, this.appConfig.getCrashHandler());

        // 适配配置
        try {
            ApplicationInfo appInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(),
                    PackageManager.GET_META_DATA);
            appInfo.metaData.putInt("design_width_in_dp", this.appConfig.getDesignWidthInDp());
            appInfo.metaData.putInt("design_height_in_dp", this.appConfig.getDesignHeightInDp());
            AutoSize.initCompatMultiProcess(this.context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getChannel() {
        if (null == this.context) {
            return "";
        }
        return WalleChannelReader.getChannel(this.context, "");
    }

    public Context getContext() {
        return context;
    }

    public AppConfig getAppConfig() {
        if (appConfig == null) {
            throw new IllegalArgumentException("appConfig is null");
        }
        return appConfig;
    }
}
