package com.rx.rxmvvmlib.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.rx.rxmvvmlib.R;


/**
 * Created by wuwei
 * 2018/12/29
 * 佛祖保佑       永无BUG
 */
public class IgnoreBatteryOptUtil {

    /**
     * 小米：
     *      自启动：授权管理--自启动管理--ZAI--开启 //完成
     * 	 省电策略：MIUI9/10：设置--电量和性能--应用配置--无限制 //完成
     *
     *
     *
     * 华为：
     *     自启动：设置--电池--启动管理--ZAI--手动管理  //完成
     * 	省电策略：无
     *
     * oppo：自启动：手机管家--自启动管理--ZAI  //完成
     * 	  省电策略：主流：设置--电池--耗电保护--关闭后台冻结和自动优化  //无法唤起
     * 	            新版：设置--电池--关闭深度省电和智能保护--自定义耗电保护--ZAI--允许后台运行--智能省电场景--关闭睡眠模式  //暂无
     *
     *
     * vivo：自启动：i管家--应用管理--权限管理--自启动--ZAI  //无法唤起
     * 	  省电策略：设置--电池--后台高耗电--ZAI //可以唤起
     *
     *
     * 三星：自启动：智能管理器--自动运行应用程序--ZAI //无法唤起
     * 	  省电策略：智能管理器--电池--未监视的应用程序--添加应用程序--ZAI //无法唤起
     */
    /**
     * Get Mobile Type
     *
     * @return
     */
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    /**
     * Compatible Mainstream Models 兼容市面主流机型
     *
     * @param context
     */
    public static void openIgnoreBatteryOpt(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = null;
            if (RomUtil.isXiaomi()) {
                componentName = new ComponentName("com.miui.powerkeeper",
                        "com.miui.powerkeeper.ui.HiddenAppsConfigActivity");
                intent.putExtra("package_name", context.getPackageName());
                intent.putExtra("package_label", context.getResources().getString(R.string.app_name));
            } else if (RomUtil.isHuawei()) {
                //无
            } else if (RomUtil.isOppo()) {
                String property = SystemUtils.getSystemProperty("ro.build.version.opporom");
                if (RomUtil.getOppoVer(property) < 5.0) {
                    componentName = new ComponentName("com.coloros.oppoguardelf",
                            "com.coloros.powermanager.fuelgaue.PowerConsumptionActivity");
                } else {
                    intent = new Intent(Settings.ACTION_SETTINGS);
                    context.startActivity(intent);
                }
            } else if (RomUtil.isVivo()) {
                componentName = new ComponentName("com.vivo.abe",
                        "com.vivo.applicationbehaviorenginev4.ui.ExcessivePowerManagerActivity");
            } else if (RomUtil.isSamsung()) {
                componentName = new ComponentName("com.samsung.android.sm_cn",
                        "com.samsung.android.sm.ui.battery.BatteryActivity");
            } else {
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            //抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }
}
