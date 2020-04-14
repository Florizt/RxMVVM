package com.rx.rxmvvmlib.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by wuwei
 * 2018/12/29
 * 佛祖保佑       永无BUG
 */
public class SelfStartUtil {

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
     * GoTo Open Self Setting Layout
     * Compatible Mainstream Models 兼容市面主流机型
     *
     * @param context
     */
    public static void openSelfStart(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = null;
            if (RomUtil.isXiaomi()) {
                componentName = new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (RomUtil.isHuawei()) {
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");//跳自启动管理
            } else if (RomUtil.isOppo()) {
                componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
                Intent intentOppo = new Intent();
                intentOppo.setClassName("com.oppo.safe/.permission.startup", "StartupAppListActivity");
                if (context.getPackageManager().resolveActivity(intentOppo, 0) == null) {
                    componentName = ComponentName.unflattenFromString("com.coloros.safecenter" + "/.startupapp.StartupAppListActivity");
                }

            } else if (RomUtil.isVivo()) {
                componentName = new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.PurviewTabActivity");
            } else if (RomUtil.isSamsung()) {
                componentName = new ComponentName("com.samsung.android.sm_cn",
                        "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity");
            } else if (RomUtil.isMeizu()) {
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.SmartBGActivity");
            } else if (RomUtil.isLetv()) {
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (RomUtil.is360()) {
                componentName = new ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity");
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
