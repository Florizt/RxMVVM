package com.rx.rxmvvmlib.util;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by wuwei
 * 2021/5/24
 * 佛祖保佑       永无BUG
 */
public class ToastUtil {
    public static void showToast(final Context context, final String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        } else {
            UIUtil.postTask(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
