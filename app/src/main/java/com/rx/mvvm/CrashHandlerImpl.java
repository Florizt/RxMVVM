package com.rx.mvvm;

import android.content.Context;
import android.util.Log;

import com.rx.rxmvvmlib.base.ICrashHandler;

/**
 * Created by wuwei
 * 2020/12/28
 * 佛祖保佑       永无BUG
 */
public class CrashHandlerImpl implements ICrashHandler {
    @Override
    public void reportError(Context context, Throwable ex) {
        Log.i("TAG", "reportErrosasar: ");
    }
}
