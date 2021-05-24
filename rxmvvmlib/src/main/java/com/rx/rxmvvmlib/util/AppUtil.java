package com.rx.rxmvvmlib.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */
public class AppUtil {

    /**
     * 判断是否是Android Q版本
     *
     * @return
     */
    public static boolean checkedAndroid_Q() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return String.valueOf(appVersionCode);
    }

    /**
     * 返回Manifest指定meta-data值
     *
     * @param context 全局context
     * @param key     meta-data key
     * @return 成功-value
     * 失败-""
     */
    public static String getMetaData(Context context, String key) {
        ApplicationInfo app_info = null;
        try {
            app_info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (app_info == null || app_info.metaData == null) {
                return "";
            } else {
                Object obj = app_info.metaData.get(key);
                if (obj != null) {
                    return obj.toString();
                } else {
                    return "";
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        try {
            final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (TextUtils.isEmpty(manager.getDeviceId())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    return manager.getDeviceId(0);
                }
            } else {
                return manager.getDeviceId();
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return new PackageInfo();
    }

    /**
     * 获取电量
     *
     * @param context
     * @return
     */
    public static int getBattery(Context context) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    /**
     * 是否在充电
     *
     * @param context
     * @return
     */
    public static boolean isCharging(Context context) {
        Intent batteryBroadcast = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        // 0 means we are discharging, anything else means charging
        boolean isCharging = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
        return isCharging;
    }

    /**
     * 检测手机是否Rooted
     *
     * @return
     */
    public static boolean isRooted(Context context) {
        boolean isSdk = isGoogleSdk(context);
        Object tags = Build.TAGS;
        if ((!isSdk) && (tags != null)
                && (((String) tags).contains("test-keys"))) {
            return true;
        }
        if (new File("/system/app/Superuser.apk").exists()) {
            return true;
        }
        if ((!isSdk) && (new File("/system/xbin/su").exists())) {
            return true;
        }
        return false;
    }

    public static boolean isGoogleSdk(Context context) {
        String str = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return ("sdk".equals(Build.PRODUCT))
                || ("google_sdk".equals(Build.PRODUCT)) || (str == null);
    }

    //---------------------------------------------memory----------------------------------------------

    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long getTotalMemory() {
        long totalMemory = -1;
        long total = 0L;
        String str;
        try {
            if (!TextUtils.isEmpty(str = parseFile(
                    new File("/proc/meminfo"), "MemTotal"))) {
                str = str.toUpperCase(Locale.US);
                if (str.endsWith("KB")) {
                    total = getSize(str, "KB", 1024);
                } else if (str.endsWith("MB")) {
                    total = getSize(str, "MB", 1048576);
                } else if (str.endsWith("GB")) {
                    total = getSize(str, "GB", 1073741824);
                } else {
                    total = -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        totalMemory = total;
        return totalMemory;
    }

    public static String parseFile(File file, String filter) {
        String str = null;
        if (file.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file), 1024);
                String line;
                while ((line = br.readLine()) != null) {
                    Pattern pattern = Pattern.compile("\\s*:\\s*");
                    String[] ret = pattern.split(line, 2);
                    if (ret != null && ret.length > 1 && ret[0].equals(filter)) {
                        str = ret[1];
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public static long getSize(String size, String uint, int factor) {
        return Long.parseLong(size.split(uint)[0].trim()) * factor;
    }

    public static String getRam(Context context) {
        long total = getTotalMemory();
        long avail = getAvailMemory(context);
        if (total <= 0) {
            return "--";
        } else {
            float ratio = (float) ((avail * 100) / total);
            return String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total));
        }
    }

    public static String getSizeWithUnit(long size) {
        if (size >= 1073741824) {
            float i = (float) (size / 1073741824);
            return String.format(Locale.US, "%.02f GB", i);
        } else if (size >= 1048576) {
            float i = (float) (size / 1048576);
            return String.format(Locale.US, "%.02f MB", i);
        } else {
            float i = (float) (size / 1024);
            return String.format(Locale.US, "%.02f KB", i);
        }
    }
    //----------------------------------------------------------------------------------------------------

    //-------------------------------------------------cache-----------------------------------------------------

    /**
     * 获取当前缓存
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static long getTotalCacheSize(Context context) {
        try {
            long cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
            return cacheSize;
        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * 清空缓存
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
    //----------------------------------------------------------------------------------------------------
}
