package com.rx.rxmvvmlib.ui.base;

import android.app.Activity;

import java.lang.ref.SoftReference;
import java.util.Stack;

import androidx.fragment.app.Fragment;

/**
 * activity堆栈式管理
 */
public class AppManager {

    private SoftReference<Stack<Activity>> activityStack = new SoftReference<>(new Stack<Activity>());
    private SoftReference<Stack<Fragment>> fragmentStack = new SoftReference<>(new Stack<Fragment>());
    private static volatile AppManager instance;

    private AppManager() {

    }

    /**
     * 单例模式
     *
     * @return AppManager
     */
    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack.get();
    }

    public Stack<Fragment> getFragmentStack() {
        return fragmentStack.get();
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack.get() != null) {
            activityStack.get().add(activity);
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack.get() != null) {
            activityStack.get().remove(activity);
        }
    }


    /**
     * 是否有activity
     */
    public boolean isActivity() {
        return activityStack.get() != null && !activityStack.get().isEmpty();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        try {
            if (activityStack.get() != null) {
                return activityStack.get().lastElement();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack.get() != null) {
            finishActivity(activityStack.get().lastElement());
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            removeActivity(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack.get() != null) {
            for (Activity activity : activityStack.get()) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack.get() != null) {
            for (int i = 0, size = activityStack.get().size(); i < size; i++) {
                if (null != activityStack.get().get(i)) {
                    finishActivity(activityStack.get().get(i));
                }
            }
            activityStack.get().clear();
        }
    }

    /**
     * 获取指定的Activity
     *
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack.get() != null) {
            for (Activity activity : activityStack.get()) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }


    /**
     * 添加Fragment到堆栈
     */
    public void addFragment(Fragment fragment) {
        if (fragmentStack.get() != null) {
            fragmentStack.get().add(fragment);
        }
    }

    /**
     * 移除指定的Fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragmentStack.get() != null) {
            fragmentStack.get().remove(fragment);
        }
    }


    /**
     * 是否有Fragment
     */
    public boolean isFragment() {
        return fragmentStack.get() != null && !fragmentStack.get().isEmpty();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Fragment currentFragment() {
        if (fragmentStack.get() != null) {
            return fragmentStack.get().lastElement();
        }
        return null;
    }


    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            //          android.os.Process.killProcess(android.os.Process.myPid());
            //            调用 System.exit(n) 实际上等效于调用：
            //            Runtime.getRuntime().exit(n)
            //            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
            //            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            //System.exit(0);
        } catch (Exception e) {
            if (activityStack.get() != null) {
                activityStack.get().clear();
            }
            e.printStackTrace();
        }
    }
}