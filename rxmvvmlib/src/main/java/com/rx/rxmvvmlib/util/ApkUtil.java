package com.rx.rxmvvmlib.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 * Created by wuwei
 * 2021/3/10
 * 佛祖保佑       永无BUG
 */
public class ApkUtil {
    public static boolean isInstall(Context context, String packageName, boolean ignoreCase) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        boolean install = false;
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                if (ignoreCase) {
                    if (packName.equalsIgnoreCase(packageName)) {
                        install = true;
                        break;
                    }
                } else {
                    if (packName.equals(packageName)) {
                        install = true;
                        break;
                    }
                }
            }
        }
        return install;
    }

    /**
     * 检测是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAliPayInstall(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWechatInstall(Context context) {
        return isInstall(context, "com.tencent.mm", false);
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientInstall(Context context) {
        return isInstall(context, "com.tencent.qqlite", true)
                || isInstall(context, "com.tencent.mobileqq", true);
    }

    /**
     * sina
     * 判断是否安装新浪微博
     */
    public static boolean isSinaInstall(Context context) {
        return isInstall(context, "com.sina.weibo", false);
    }

    public static boolean isGdMapInstall(Context context) {
        return isInstall(context, "com.autonavi.minimap", false);
    }

    public static boolean isBaiduMapInstall(Context context) {
        return isInstall(context, "com.baidu.BaiduMap", false);
    }

    public static boolean isTencentMapInstall(Context context) {
        return isInstall(context, "com.tencent.map", false);
    }
}
