package com.rx.mvvm;

import android.app.Application;
import android.content.Context;

import com.rx.rxmvvmlib.RxMVVMInit;
import com.rx.rxmvvmlib.base.ICrashHandler;

/**
 * Created by wuwei
 * 2020/4/14
 * 佛祖保佑       永无BUG
 */
public class App extends Application implements ICrashHandler {
    @Override
    public void onCreate() {
        super.onCreate();
        RxMVVMInit.getInstance().init(this);
    }


    @Override
    public void reportError(Context context, Throwable ex) {

    }
}
