package com.rx.mvvm;

import android.util.Log;

import com.rx.rxmvvmlib.base.IActivityLifecycleCallbacks;

/**
 * Created by wuwei
 * 2020/12/31
 * 佛祖保佑       永无BUG
 */
public class ActivityLifecycleCallbacksImpl extends IActivityLifecycleCallbacks {
    @Override
    public void back2App() {
        Log.i("TAG", "back2Apsasp-111: ");
    }

    @Override
    public void leaveApp() {
        Log.i("TAG", "back2Apsasp-222: ");
    }
}
