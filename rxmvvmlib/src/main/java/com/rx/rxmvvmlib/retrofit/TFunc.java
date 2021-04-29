package com.rx.rxmvvmlib.retrofit;

import android.text.TextUtils;

import com.rx.rxmvvmlib.RxMVVMInit;
import com.rx.rxmvvmlib.annotation.HttpCode;
import com.rx.rxmvvmlib.annotation.HttpData;
import com.rx.rxmvvmlib.annotation.HttpMsg;
import com.rx.rxmvvmlib.util.LogUtil;

import java.lang.reflect.Field;

import io.reactivex.functions.Function;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */


public class TFunc<T, D> implements Function<T, D> {

    public TFunc() {

    }

    @Override
    public D apply(T tReply) throws Exception {
        Object httpCode = null;
        Object httpMsg = null;
        Object httpData = null;
        Field[] fields = tReply.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(HttpCode.class)) {
                httpCode = field.get(tReply);
            } else if (field.isAnnotationPresent(HttpMsg.class)) {
                httpMsg = field.get(tReply);
            } else if (field.isAnnotationPresent(HttpData.class)) {
                httpData = field.get(tReply);
            }
        }

        if (TextUtils.equals(RxMVVMInit.getConfig().getHttpSuccessCode(), String.valueOf(httpCode))) {
            LogUtil.e("请求成功");
            return (D) httpData;
        } else {
            LogUtil.e("请求失败");
            throw new ResultException(String.valueOf(httpCode), String.valueOf(httpMsg));
        }
    }
}
