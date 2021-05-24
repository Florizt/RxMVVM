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

import java.util.List;

/**
 * 显示通知栏工具类
 * Created by Administrator on 2016-11-14.
 */

public class NotificationUtil {

    public static Notification buildNotification(Context context,
                                                 int icon, String title, String contentText,
                                                 String channelId, String channelName) {
        return buildNotification(context, icon, title, contentText, channelId, channelName, null, 0);
    }

    public static Notification buildNotification(Context context,
                                                 int icon, String title, String contentText,
                                                 String channelId, String channelName,
                                                 Class clazz, int requestCode) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return null;
        }

        Notification.Builder builder = new Notification.Builder(context)
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{0})
                .setSound(null, null)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(contentText);

        if (clazz != null) {
            Intent intent = new Intent();
            intent.setClass(context, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(buildChannel(channelId, channelName));
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

    public static void deleteNotification(Context context, int notificationId, String channelId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(channelId);
        }
        notificationManager.cancel(notificationId);
    }

    public static void deleteAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancelAll();
        try {
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
                Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
                intent.putExtra("packageName", context.getPackageName());
                String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
                intent.putExtra("className", launchClassName);
                intent.putExtra("notificationNum", 0);
                context.sendBroadcast(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
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
