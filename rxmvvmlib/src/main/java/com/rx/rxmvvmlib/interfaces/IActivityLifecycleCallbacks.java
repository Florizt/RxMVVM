package com.rx.rxmvvmlib.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rx.rxmvvmlib.base.AppManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by wuwei
 * 2020/12/31
 * 佛祖保佑       永无BUG
 */
public abstract class IActivityLifecycleCallbacks {
    private int appCount;
    private boolean isRunInBackground;

    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(activity);
    }


    public void onActivityStarted(@NonNull Activity activity) {
        appCount++;
        if (isRunInBackground) {
            //应用从后台回到前台 需要做的操作
            isRunInBackground = false;
            back2App(activity);
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
            isRunInBackground = true;
            leaveApp(activity);
        }
    }


    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    public void onActivityResult(@NonNull Activity activity,int requestCode, int resultCode, @Nullable Intent data) {

    }


    public void onActivityDestroyed(@NonNull Activity activity) {
        AppManager.getAppManager().removeActivity(activity);
    }

    public void onAppExit(@NonNull Activity activity) {

    }

    public abstract void back2App(Context context);

    public abstract void leaveApp(Context context);
}
