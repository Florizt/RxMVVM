package com.rx.rxmvvmlib.http;


import com.rx.rxmvvmlib.util.LanguageUtil;
import com.rx.rxmvvmlib.util.SystemUtils;
import com.rx.rxmvvmlib.util.UIUtils;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("req-id", UUID.randomUUID().toString())
                .addHeader("app-ver", SystemUtils.getVersion())
                .addHeader("os", "0")
                .addHeader("token", UUID.randomUUID().toString())
                .addHeader("uid",  UUID.randomUUID().toString())
                .addHeader("lang", LanguageUtil.isZh(UIUtils.getContext()) ? "zh_cn" : "en")
                .build();
        return chain.proceed(request);
    }
}
