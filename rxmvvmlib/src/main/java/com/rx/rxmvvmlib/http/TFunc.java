package com.rx.rxmvvmlib.http;

import android.text.TextUtils;

import com.dgrlucky.log.LogX;
import com.rx.rxmvvmlib.RxMVVMInit;

import io.reactivex.functions.Function;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */


public class TFunc<T> implements Function<HttpResult<T>, T> {


    public TFunc() {

    }


    @Override
    public T apply(HttpResult<T> tReply) throws Exception {
        String error_code = tReply.getCode();
        if (!TextUtils.equals(RxMVVMInit.config.httpSuccessCode, error_code)) {
            if (RxMVVMInit.config.debugEnable) {
                LogX.e("请求失败");
            }
            throw new ResultException(tReply.getMessage(), tReply.getCode());
        } else {
            if (RxMVVMInit.config.debugEnable) {
                LogX.e("请求成功");
            }
            return tReply.getData();
        }
    }
}
