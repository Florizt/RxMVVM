package com.rx.rxmvvmlib.util.keyboardutil.callback;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.rx.rxmvvmlib.util.LogUtil;
import com.rx.rxmvvmlib.util.NotchUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.FitWindowsLinearLayout;

/**
 * 用于真正的监听布局变化的回调类
 *
 * @author Simon
 */

public class GlobalLayoutListenerImp implements ViewTreeObserver.OnGlobalLayoutListener {

    private Activity activity;
    private View mView;
    private List<IkeyBoardCallback> ikeyBoardCallbackList;
    private final int NONE = -1;
    private final int SHOW = 1;
    private final int HIDDEN = 2;
    private int status = NONE;


    public GlobalLayoutListenerImp(Activity activity, View view) {
        this.activity = activity;
        mView = view;
        ikeyBoardCallbackList = new ArrayList<>();
    }

    @Override
    public void onGlobalLayout() {
        //activity为null不执行
        if (activity != null && mView != null) {
            //获取可视范围
            Rect r = new Rect();
            mView.getWindowVisibleDisplayFrame(r);

            //获取被遮挡高度（键盘高度）(屏幕高度-状态栏高度-可视范围)
            int keyBoardHeight = getScreenAvailableHeight() - (r.height() + (isStatusBarShown() ? getStatusBarHeight() : 0));

            //显示或者隐藏
            boolean isShowKeyBoard = keyBoardHeight > (getScreenAvailableHeight() / 4);

            LogUtil.i("sas", "onGlobasalLayout: ---" + r.height() + "  " + getScreenAvailableHeight() + "  "
                    + getScreenHeight2() + "  " + getNavigationBarHeight2() + "  " + getStatusBarHeight() + "   "
                    + isShowKeyBoard + "  " + keyBoardHeight + "  " + status + "  " + isStatusBarShown());
            //当首次或者和之前的状态不一致的时候会回调，反之不回调(用于当状态变化后才回调，防止多次调用)
            if (status == NONE || (isShowKeyBoard && status == HIDDEN) || (!isShowKeyBoard && status == SHOW)) {
                if (isShowKeyBoard) {
                    status = SHOW;
                    dispatchKeyBoardShowEvent(keyBoardHeight);
                } else {
                    status = HIDDEN;
                    dispatchKeyBoardHiddenEvent();
                }
            }
        }
    }

    public boolean isStatusBarShown() {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        int paramsFlag = params.flags & (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return paramsFlag == params.flags;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.widthPixels;
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.heightPixels;
    }

    public int getScreenAvailableHeight() {
        return getScreenHeight() - getNavigationBarHeight2();
    }

    public int getScreenHeight2() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public int getNavigationBarHeight2() {
        ViewGroup rootLinearLayout = findRootLinearLayout();
        int navigationBarHeight = 0;
        if (rootLinearLayout != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootLinearLayout.getLayoutParams();
            navigationBarHeight = layoutParams.bottomMargin;
        }
        return navigationBarHeight;
    }

    private ViewGroup findRootLinearLayout() {
        ViewGroup onlyLinearLayout = null;
        try {
            Window window = activity.getWindow();
            if (window != null) {
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
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


    public int getStatusBarHeight() {
        int height = 0;
        if (NotchUtils.hasNotchAtXiaomi()) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        } else if (NotchUtils.hasNotchAtHuawei(activity)) {
            height = NotchUtils.getNotchSizeAtHuawei(activity)[1];
        } else if (NotchUtils.hasNotchAtVivo(activity)) {
            height = dip2px(27);
        } else if (NotchUtils.hasNotchAtOPPO(activity)) {
            height = 80;
        } else {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public int dip2px(float dip) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * 添加监听回调
     *
     * @param callback 监听的回调类
     */
    public void addCallback(Object callback) {
        if (ikeyBoardCallbackList != null && callback instanceof IkeyBoardCallback) {
            ikeyBoardCallbackList.add((IkeyBoardCallback) callback);
        }
    }

    /**
     * 移除监听回调
     *
     * @param callback 监听的回调类
     */
    public void removeCallback(Object callback) {
        if (ikeyBoardCallbackList != null) {
            ikeyBoardCallbackList.remove(callback);
        }
    }

    /**
     * 判断是不是没有监听回调
     *
     * @return true:空 false:不空
     */
    public boolean isEmpty() {
        if (ikeyBoardCallbackList == null) {
            return true;
        }
        return ikeyBoardCallbackList.isEmpty();
    }

    /**
     * 清除内部内存引用
     */
    public void release() {
        status = NONE;
        activity = null;
        ikeyBoardCallbackList.clear();
        ikeyBoardCallbackList = null;
    }

    /**
     * 分发隐藏事件
     */
    private void dispatchKeyBoardHiddenEvent() {
        if (ikeyBoardCallbackList != null) {
            for (IkeyBoardCallback callback : ikeyBoardCallbackList) {
                callback.onKeyBoardHidden();
            }
        }
    }

    /**
     * 分发显示事件
     */
    private void dispatchKeyBoardShowEvent(int h) {
        if (ikeyBoardCallbackList != null) {
            for (IkeyBoardCallback callback : ikeyBoardCallbackList) {
                callback.onKeyBoardShow(h);
            }
        }
    }


}
