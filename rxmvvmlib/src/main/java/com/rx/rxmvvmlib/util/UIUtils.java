package com.rx.rxmvvmlib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.FitWindowsLinearLayout;

/**
 * Created by 吴巍 on 2017/9/5.
 */

public class UIUtils {

    private static Context sContext;
    private static Handler sHandler;
    public static float sDensity;
    public static float sScaleDensity;

    public static void init(Context context) {
        sContext = context;
        sHandler = new Handler();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        sDensity = dm.density;
        sScaleDensity = dm.scaledDensity;
    }

    public static Context getContext() {
        return sContext.getApplicationContext();
    }

    public static Handler getHandler() {
        return sHandler;
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

    public static int getDimens(int id) {
        return getContext().getResources().getDimensionPixelOffset(id);
    }

    public static void postTask(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void postDelayTask(Runnable runnable, long time) {
        sHandler.postDelayed(runnable, time);
    }

    public static void removeTask(Runnable runnable) {
        sHandler.removeCallbacks(runnable);
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

    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId).mutate();
    }

    public static AssetManager getAsstManager() {
        return getContext().getAssets();
    }

    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    public static void showToast(final String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (isMainThread()) {
            Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show();
        } else {
            postTask(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }

    public static void translucentStatusBar(Activity activity) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getScreenAvailableHeight(Context context) {
        return getScreenHeight() - getNavigationBarHeight2(context);
    }

    /**
     * @return false 关闭了NavgationBar ,true 开启了
     */
    public static boolean navgationbarIsOpen(Context context) {
        ViewGroup rootLinearLayout = findRootLinearLayout(context);
        int navigationBarHeight = 0;

        if (rootLinearLayout != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootLinearLayout.getLayoutParams();
            navigationBarHeight = layoutParams.bottomMargin;
        }
        if (navigationBarHeight == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 导航栏高度，关闭的时候返回0,开启时返回对应值
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight2(Context context) {
        ViewGroup rootLinearLayout = findRootLinearLayout(context);
        int navigationBarHeight = 0;
        if (rootLinearLayout != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootLinearLayout.getLayoutParams();
            navigationBarHeight = layoutParams.bottomMargin;
        }
        return navigationBarHeight;
    }

    /**
     * 从R.id.content从上遍历，拿到 DecorView 下的唯一子布局LinearLayout
     * 获取对应的bottomMargin 即可得到对应导航栏的高度，0为关闭了或没有导航栏
     */
    private static ViewGroup findRootLinearLayout(Context context) {
        ViewGroup onlyLinearLayout = null;
        try {
            Window window = getWindow(context);
            if (window != null) {
                ViewGroup decorView = (ViewGroup) getWindow(context).getDecorView();
                Activity activity = getActivity(context);
                View contentView = activity.findViewById(android.R.id.content);
                if (contentView != null) {
                    View tempView = contentView;
                    while (tempView.getParent() != decorView) {
                        ViewGroup parent = (ViewGroup) tempView.getParent();
                        if (parent instanceof LinearLayout) {
                            //如果style设置了不带toolbar,mContentView上层布局会由原来的 ActionBarOverlayLayout->FitWindowsLinearLayout)
                            if (parent instanceof FitWindowsLinearLayout) {
                                tempView = parent;
                                continue;
                            } else {
                                onlyLinearLayout = parent;
                                break;
                            }
                        } else {
                            tempView = parent;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return onlyLinearLayout;
    }


    private static Window getWindow(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context).getWindow();
        } else {
            return scanForActivity(context).getWindow();
        }
    }

    private static Activity getActivity(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context);
        } else {
            return scanForActivity(context);
        }
    }


    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null)
            return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    private static Activity scanForActivity(Context context) {
        if (context == null)
            return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 判断底部navigator是否已经显示
     *
     * @param windowManager
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;


        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasSoftKeys((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    public static int getStatusBarHeight() {
        int height = 0;
        if (NotchUtils.hasNotchAtXiaomi()) {
            Resources resources = UIUtils.getContext().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        } else if (NotchUtils.hasNotchAtHuawei(getContext())) {
            height = NotchUtils.getNotchSizeAtHuawei(getContext())[1];
        } else if (NotchUtils.hasNotchAtVivo(getContext())) {
            height = UIUtils.dip2px(27);
        } else if (NotchUtils.hasNotchAtOPPO(getContext())) {
            height = 80;
        } else {
            Resources resources = UIUtils.getContext().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * Description:设置状态栏图标是否为黑色
     * Date:2018/3/22
     */
    public static void setStatusBarStyle(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(
                    dark ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0);
        }
    }





    public static String getResourcesUri(@DrawableRes int id) {
        Resources resources = getContext().getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }
}
