package com.rx.rxmvvmlib.util.keyboardutil.core;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.util.keyboardutil.callback.GlobalLayoutListenerImp;

import java.util.HashMap;
import java.util.Map;


/**
 * 用于管理键盘事件
 *
 * @author Simon
 */
public final class KeyBoardEventBus {

    /**
     * TAG
     */
    private static final String TAG = "KeyBoardEventBus";
    /**
     * 单例
     */
    private static KeyBoardEventBus instance = new KeyBoardEventBus();

    /**
     * 用于缓存监听回调信息
     */
    private Map<Object, GlobalLayoutListenerImp> callbackCache = new HashMap<>();

    /**
     * 状态栏高度
     */
    private int statusBarHeight = -1;

    /**
     * 全屏时的高度
     */
    private int fullScreenHeight = -1;
    private PopupWindow mPopupWindow;
    private View mPopupView;

    /**
     * 私有化构造函数
     */
    private KeyBoardEventBus() {

    }

    /**
     * 用于外部获取KeyBoardEventBus 对象
     *
     * @return 返回 KeyBoardEventBus 对象
     */
    public static KeyBoardEventBus getDefault() {
        return instance;
    }


    /**
     * 用于注册键盘监听，此方法适用于 View、Dialog、Fragement(v4、app)、FragementActivity、Activity
     *
     * @param object 需要监听的类（）
     */
    public void register(Object object) {
        //获取失败则直接停止，反之进行注册
        Activity activity = getActivity(object);
        if (activity != null) {
            register(activity, object);
        }
    }

    /**
     * 此方法区别于 {@link #register(Object)} ,之前的方法会限制注册的类型，当前的不会限制类型
     *
     * @param activity 宿主activity
     * @param object   监听的类
     */
    public void register(final Activity activity, final Object object) {
        if (activity != null && object != null) {
            final View parentView = activity.findViewById(R.id.r);
            if (parentView != null) {
                parentView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPopupWindow = new PopupWindow();

                        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        mPopupView = inflator.inflate(R.layout.softkeyboard_popupwindow, null, false);
                        mPopupWindow.setContentView(mPopupView);
                        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
                        mPopupWindow.setWidth(0);
                        mPopupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

                        GlobalLayoutListenerImp imp = callbackCache.get(activity);
                        if (imp == null) {
                            imp = new GlobalLayoutListenerImp(activity, mPopupView);
                        }

                        imp.addCallback(object);

                        mPopupView.getViewTreeObserver().addOnGlobalLayoutListener(imp);

                        //如果不是空
                        if (!imp.isEmpty()) {
                            if (!mPopupWindow.isShowing() && parentView.getWindowToken() != null) {
                                mPopupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
                            }

                            //缓存
                            callbackCache.put(activity, imp);
                        }
                    }
                });
            }
        }
    }

    /**
     * 反注册
     *
     * @param object 取消监听的类
     */
    public void unregister(Object object) {
        //获取失败则直接停止，反之进行反注册
        Activity activity = getActivity(object);
        if (activity != null) {
            unregister(activity, object);
        }
    }

    /**
     * 反注册
     *
     * @param activity 宿主activity
     * @param object   监听的类
     */
    public void unregister(Activity activity, Object object) {
        if (activity != null && object != null) {
            GlobalLayoutListenerImp imp = callbackCache.get(activity);
            if (imp != null) {
                //移除对应的回调
                imp.removeCallback(object);

                //如果回调集合为空则直接移除
                if (imp.isEmpty()) {
                    //去掉监听
                    if (mPopupView != null) {
                        mPopupView.getViewTreeObserver().removeOnGlobalLayoutListener(imp);
                    }

                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                    //释放监听内缓存、引用
                    imp.release();
                    //释放缓存
                    callbackCache.remove(activity);
                }
            }
        }
    }


    /**
     * 获取对应View、Dialog、Fragement(v4、app)、FragementActivity、Activity 的Activity
     * (如果Object为null或者不是支持的类型则返回null)
     *
     * @param object 需要获取的类
     * @return 返回对应的activity
     */
    public Activity getActivity(Object object) {

        if (object == null) {
            return null;
        }

        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof androidx.fragment.app.Fragment) {
            return ((androidx.fragment.app.Fragment) object).getActivity();
        } else if (object instanceof Dialog) {
            return (Activity) ((Dialog) object).getContext();
        } else if (object instanceof View) {
            return (Activity) ((View) object).getContext();
        }

        return null;

    }
}
