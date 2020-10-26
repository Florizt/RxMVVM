package com.rx.mvvm;

import android.app.Application;
import android.content.Context;

import com.rx.rxmvvmlib.RxMVVMInitializer;
import com.rx.rxmvvmlib.config.AppConfig;
import com.rx.rxmvvmlib.listener.ICrashHandler;

import java.util.HashMap;

/**
 * Created by wuwei
 * 2020/4/14
 * 佛祖保佑       永无BUG
 */
public class App extends Application implements ICrashHandler {
    @Override
    public void onCreate() {
        super.onCreate();
        AppConfig appConfig = new AppConfig();
        appConfig.setCrashHandler(this);
        appConfig.setDesignWidthInDp(360);
        appConfig.setDesignHeightInDp(640);
        appConfig.setApiService(new HttpApi() {});
        appConfig.setHttpHostName("sas");
        appConfig.setHttpDebugUrl("sas");
        appConfig.setHttpReleaseUrl("sas");
        appConfig.setHttpSuccessCode("0");
        appConfig.setHeader(new HashMap<String, String>());
        RxMVVMInitializer.getInstance().init(this, appConfig);
    }


    @Override
    public void reportError(Context context, Throwable ex) {

    }
}
