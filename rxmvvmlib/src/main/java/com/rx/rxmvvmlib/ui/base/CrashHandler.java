package com.rx.rxmvvmlib.ui.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.rx.rxmvvmlib.ui.ICrashHandler;
import com.rx.rxmvvmlib.util.FileUtil;
import com.rx.rxmvvmlib.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.content.ContextCompat;

/**
 * Created by wuwei
 * 2020/1/4
 * 佛祖保佑       永无BUG
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler defaultCrashHandler;
    private Context context;
    private ICrashHandler handler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context, ICrashHandler handler) {
        this.context = context.getApplicationContext();
        this.handler = handler;
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaught-
     * Exception方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异
     * 常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决bug
            if (handler != null) {
                handler.reportError(context, ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (defaultCrashHandler != null) {
            defaultCrashHandler.uncaughtException(thread, ex);
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            LogUtil.w(TAG, "sdcard unmounted,skip dump exception");
            return;
        }

        try {
            long current = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
            File file = new File(FileUtil.createDir(context, FileUtil.TYPE_DOCUMENT), FILE_NAME + time + FILE_NAME_SUFFIX);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            LogUtil.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        //Android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }
}