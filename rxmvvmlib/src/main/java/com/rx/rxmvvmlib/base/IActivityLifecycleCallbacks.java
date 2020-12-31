package com.rx.rxmvvmlib.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by wuwei
 * 2020/12/31
 * 佛祖保佑       永无BUG
 */
public class IActivityLifecycleCallbacks {
    private int appCount;
    private boolean isRunInBackground;

    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(activity);
    }


    public void onActivityStarted(@NonNull Activity activity) {
        appCount++;
        if (isRunInBackground) {
            //应用从后台回到前台 需要做的操作
            back2App();
        }
    }


    public void onActivityResumed(@NonNull Activity activity) {

    }


    public void onActivityPaused(@NonNull Activity activity) {

    }


    public void onActivityStopped(@NonNull Activity activity) {
        appCount--;
        if (appCount == 0) {
            //应用进入后台 需要做的操作
            leaveApp();
        }
    }


    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }


    public void onActivityDestroyed(@NonNull Activity activity) {
        AppManager.getAppManager().removeActivity(activity);
    }

    public void back2App() {

    }

    public void leaveApp() {

    }
}
