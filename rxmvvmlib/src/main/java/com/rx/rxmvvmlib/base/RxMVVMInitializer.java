package com.rx.rxmvvmlib.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.meituan.android.walle.WalleChannelReader;
import com.rx.rxmvvmlib.util.UIUtils;

import me.jessyan.autosize.AutoSize;


/**
 * Created by wuwei
 * 2019/12/5
 * 佛祖保佑       永无BUG
 */
public class RxMVVMInitializer {


    private static Context context;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context, ICrashHandler handler) {
        init(context, handler, 360, 640);
    }

    /**
     * 初始化
     *
     * @param context
     * @param width   屏幕适配，底图尺寸
     * @param height  屏幕适配，底图尺寸
     */
    public static void init(Context context, ICrashHandler handler, int width, int height) {
        RxMVVMInitializer.context = context;
        // 主项目配置
        UIUtils.init(context);

        // 崩溃抓取
        CrashHandler.getInstance().init(context, handler);

        // 适配配置
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            appInfo.metaData.putInt("design_width_in_dp", width);
            appInfo.metaData.putInt("design_height_in_dp", height);
            AutoSize.initCompatMultiProcess(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getChannel() {
        if (null == context) {
            return "";
        }
        return WalleChannelReader.getChannel(context.getApplicationContext(), "");
    }
}
