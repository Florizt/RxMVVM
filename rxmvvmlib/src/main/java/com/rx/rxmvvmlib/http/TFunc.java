package com.rx.rxmvvmlib.http;

import android.text.TextUtils;

import com.dgrlucky.log.LogX;
import com.rx.rxmvvmlib.RxMVVMInit;
import com.rx.rxmvvmlib.annotation.HttpCode;
import com.rx.rxmvvmlib.annotation.HttpData;
import com.rx.rxmvvmlib.annotation.HttpMsg;

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

        if (!TextUtils.equals(RxMVVMInit.config.httpSuccessCode, String.valueOf(httpCode))) {
            if (RxMVVMInit.config.debugEnable) {
                LogX.e("请求失败");
            }
            throw new ResultException(String.valueOf(httpCode), String.valueOf(httpMsg));
        } else {
            if (RxMVVMInit.config.debugEnable) {
                LogX.e("请求成功");
            }
            return (D) httpData;
        }
    }
}
