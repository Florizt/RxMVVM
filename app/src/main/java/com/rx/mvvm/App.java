package com.rx.mvvm;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.rx.rxmvvmlib.RxMVVMInit;

/**
 * Created by wuwei
 * 2020/4/14
 * 佛祖保佑       永无BUG
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openLog();
        ARouter.openDebug();
        RxMVVMInit.getInstance().init(this);
    }
}
