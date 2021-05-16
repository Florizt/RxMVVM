package com.rx.mvvm;

import android.app.Application;

import com.rx.rxmvvmlib.aop.anno.RxMVVMInitz;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Created by wuwei
 * 2020/4/14
 * 佛祖保佑       永无BUG
 */
@HiltAndroidApp
public class App extends Application {
    @RxMVVMInitz(clazz = DemoCfgsAdapter.class)
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
