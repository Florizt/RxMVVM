package com.rx.mvvm;

import android.app.Application;

import com.rx.mvvm.aop.anno.ArouterDestroy;
import com.rx.mvvm.aop.anno.ArouterInit;
import com.rx.rxmvvmlib.aop.anno.RxMVVMInitz;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class App extends Application {
    @RxMVVMInitz(clazz = AppCfgsAdapter.class)
    @ArouterInit
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @ArouterDestroy
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
