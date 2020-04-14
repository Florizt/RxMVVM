package com.rx.rxmvvmlib.base;

import android.content.Context;

import com.rx.rxmvvmlib.util.UIUtils;

import me.jessyan.autosize.AutoSize;


/**
 * Created by wuwei
 * 2019/12/5
 * 佛祖保佑       永无BUG
 */
public class RxMVVMInitializer {

    public void init(Context context) {
        // 主项目配置
        UIUtils.init(context);

        // 崩溃抓取
        CrashHandler.getInstance().init(context);

        // 适配配置
        AutoSize.initCompatMultiProcess(context);
    }
}
