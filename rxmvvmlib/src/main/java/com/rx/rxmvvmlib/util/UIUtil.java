package com.rx.rxmvvmlib.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.io.InputStream;

import androidx.annotation.DrawableRes;

/**
 * Created by 吴巍 on 2017/9/5.
 */

public class UIUtil {

    private static Context context;
    private static Handler handler;

    public static void init(Context context) {
        UIUtil.context = context.getApplicationContext();
        UIUtil.handler = new Handler();
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static String getString(int id) {
        return getContext().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return getContext().getString(id, formatArgs);
    }

    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    public static int getDimens(int id) {
        return getContext().getResources().getDimensionPixelOffset(id);
    }

    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId).mutate();
    }

    public static AssetManager getAsstManager() {
        return getContext().getAssets();
    }

    public static InputStream openAssets(Context context, String fileName) {
        try {
            return context.getResources().getAssets().open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream openRaw(Context context, int resId) {
        try {
            return context.getResources().openRawResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResourcesUri(@DrawableRes int id) {
        Resources resources = getContext().getResources();
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
    }

    public static int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int sp2px(float sp) {
        float density = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    public static int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static void postTask(Runnable runnable) {
        getHandler().post(runnable);
    }

    public static void postDelayTask(Runnable runnable, long time) {
        getHandler().postDelayed(runnable, time);
    }

    public static void removeTask(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }
}
