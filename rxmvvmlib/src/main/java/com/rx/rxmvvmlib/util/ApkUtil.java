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
    /**
     * 检测是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWechatInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * sina
     * 判断是否安装新浪微博
     */
    public static boolean isSinaInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }

        return false;
    }
}
