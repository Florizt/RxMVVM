package com.rx.rxmvvmlib.util;

import android.content.Context;

import java.util.Locale;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class LanguageUtil {
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        if (locale.getCountry().equals("CN"))
            return true;
        else
            return false;
    }
}
