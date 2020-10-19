package com.rx.rxmvvmlib.http;


import com.rx.rxmvvmlib.RxMVVMInitializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        Request.Builder builder = chain.request().newBuilder();
        Map<String, String> header = RxMVVMInitializer.getInstance().getAppConfig().getHeader();
        if (header != null && header.size() > 0) {
            Set<Map.Entry<String, String>> set = header.entrySet();
            Iterator<Map.Entry<String, String>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return chain.proceed(builder.build());
    }
}
