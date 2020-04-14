package com.rx.rxmvvmlib.util;

import android.os.Build;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.cardview.widget.CardView;

/**
 * Created by wuwei
 * 2020/1/13
 * 佛祖保佑       永无BUG
 */
public class CardUtils {
    private static boolean inited=false;
    //设置obj的成员变量
    private static void setMember(Object obj, String memberName, Object value) {
        try {
            if (obj instanceof Class) {
                //静态变量
                Field declaredField = ((Class) obj).getDeclaredField(memberName);
                declaredField.setAccessible(true);
                declaredField.set(null, value);
            } else {
                Field declaredField = obj.getClass().getDeclaredField(memberName);
                declaredField.setAccessible(true);
                declaredField.set(obj, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (Build.VERSION.SDK_INT >= 21&&!inited) {
            inited=true;
            try {
                //new 一个CardViewApi17Impl
                Constructor<?> constructor = Class.forName("androidx.cardview.widget.CardViewApi17Impl").getDeclaredConstructor();
                constructor.setAccessible(true);
                Object impl = constructor.newInstance();

                //用新的代替掉原来的
                setMember(CardView.class, "IMPL", impl);

                //执行方法IMPL.initStatic()
                Method initStatic = impl.getClass().getDeclaredMethod("initStatic");
                initStatic.setAccessible(true);
                initStatic.invoke(impl);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void setCardShadowColor(CardView cardView, int startColor, int endColor) {
        try {
            //获取背景
            Object background = cardView.getBackground();
            //设置颜色
            setMember(background, "mShadowStartColor", startColor);
            setMember(background, "mShadowEndColor", endColor);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}