package com.rx.rxmvvmlib.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.FitWindowsLinearLayout;

/**
 * Created by wuwei
 * 2021/5/24
 * 佛祖保佑       永无BUG
 */
public class ScreenUtil {

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getAvailableScreenHeight(Context context) {
        return getScreenHeight(context) - getNavigationBarHeight(context);
    }

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        if (NotchUtil.hasNotchAtXiaomi()) {
            Resources resources = UIUtil.getContext().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        } else if (NotchUtil.hasNotchAtHuawei(context)) {
            height = NotchUtil.getNotchSizeAtHuawei(context)[1];
        } else if (NotchUtil.hasNotchAtVivo(context)) {
            height = UIUtil.dip2px(27);
        } else if (NotchUtil.hasNotchAtOPPO(context)) {
            height = 80;
        } else {
            Resources resources = UIUtil.getContext().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 导航栏高度，关闭的时候返回0,开启时返回对应值
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        ViewGroup rootLinearLayout = findRootLinearLayout(context);
        int navigationBarHeight = 0;
        if (rootLinearLayout != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootLinearLayout.getLayoutParams();
            navigationBarHeight = layoutParams.bottomMargin;
        }
        return navigationBarHeight;
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

    public static void translucentStatusBar(Activity activity) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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
}
