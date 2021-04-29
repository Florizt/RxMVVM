package com.rx.rxmvvmlib.listener;

import android.content.Context;

/**
 * Created by wuwei
 * 2020/4/18
 * 佛祖保佑       永无BUG
 */
public interface ICrashHandler {
    void reportError(Context context, Throwable ex);
}
