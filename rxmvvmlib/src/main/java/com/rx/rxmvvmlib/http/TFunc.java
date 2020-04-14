package com.rx.rxmvvmlib.http;

import com.dgrlucky.log.LogX;
import com.rx.rxmvvmlib.BuildConfig;
import com.rx.rxmvvmlib.entity.http.HttpResult;

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
        int error_code = tReply.getCode();
        if (error_code != 0) {
            if (BuildConfig.DEBUG) {
                LogX.e("请求失败");
            }
            throw new ResultException(tReply.getErr(), tReply.getCode());
        } else {
            if (BuildConfig.DEBUG) {
                LogX.e("请求成功");
            }
            return tReply.getData();
        }
    }
}
