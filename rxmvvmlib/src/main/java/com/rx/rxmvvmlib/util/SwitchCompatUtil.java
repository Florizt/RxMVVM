package com.rx.rxmvvmlib.util;

import android.content.res.ColorStateList;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Created by wuwei
 * 2019/12/19
 * 佛祖保佑       永无BUG
 */
public class SwitchCompatUtil {
    public static void setSwitchColor(SwitchCompat v) {

        // thumb color
        int thumbColor = 0xffffffff;

        // trackColor
        int trackColor = 0xfff1f1f1;

        // set the thumb color
        DrawableCompat.setTintList(v.getThumbDrawable(), new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        thumbColor,
                        thumbColor
                }));

        // set the track color
        DrawableCompat.setTintList(v.getTrackDrawable(), new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        0xffffe61e,
                        0xff828282
                }));
    }
}
