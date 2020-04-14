package com.rx.rxmvvmlib.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.rx.rxmvvmlib.R;

import java.util.List;

/**
 * 显示通知栏工具类
 * Created by Administrator on 2016-11-14.
 */

public class NotificationUtil {
    public static final String CHANNELID = "locate";
    public static final String CHANNELNAME = "location";

    public static void deleteNotification(Context context, int notificationId, String channelId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(channelId);
        }
        notificationManager.cancel(notificationId);
    }

    public static Notification buildNotification(Context context, String channelId, String channelName, Class clazz,String contentText) {
        Intent intent2 = new Intent();
        intent2.setClass(UIUtils.getContext(), clazz);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intentContent = PendingIntent.getActivity(UIUtils.getContext(), 304, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) UIUtils.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(UIUtils.getContext())
                .setContentIntent(intentContent)
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{0})
                .setSound(null, null)
                .setSmallIcon(context.getApplicationContext().getApplicationInfo().icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(contentText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(buildChannel(channelId, channelName));
            }
            builder.setChannelId(channelId);
        }
        return builder.build();
    }

    public static NotificationChannel buildChannel(String channelId, String channelName) {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.setSound(null, null);
        }
        return notificationChannel;
    }

    public static void deleteAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if (RomUtil.isSamsung()) {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", 0);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", getLaunchIntentForPackage(context));

            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            }
        } else if (RomUtil.isHuawei()) {
            Bundle bunlde = new Bundle();
            bunlde.putString("package", context.getPackageName()); // com.test.badge is your package name
            bunlde.putString("class", getLaunchIntentForPackage(context)); // com.test. badge.MainActivity is your apk main activity
            bunlde.putInt("badgenumber", 0);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
        } else if (RomUtil.isVivo()) {
            try {
                Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
                intent.putExtra("packageName", context.getPackageName());
                String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
                intent.putExtra("className", launchClassName);
                intent.putExtra("notificationNum", 0);
                context.sendBroadcast(intent);
            } catch (Exception e) {

            }
        }
    }

    public static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }

    public static String getLaunchIntentForPackage(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
    }
}
