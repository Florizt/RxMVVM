package com.rx.rxmvvmlib.http;

import com.dgrlucky.log.LogX;
import com.rx.rxmvvmlib.RxMVVMInitializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by wuwei
 * 2018/1/20
 * 佛祖保佑       永无BUG
 */

public class LoggingInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        RequestBody requestBody = request.body();

        String body = null;

        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }

        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            LogX.e("发送请求\nmethod：%s\nurl：%s\nheaders: %sbody：%s",
                    request.method(), request.url(), request.headers(), body);
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        String rBody = null;

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        rBody = buffer.clone().readString(charset);

        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            LogX.e("收到响应 %s%s %ss\n请求url：%s\nheaders: %s请求body：%s\n响应body：%s",
                    response.code(), response.message(), tookMs, response.request().url(), response.headers(), body, rBody);
        }

        return response;
    }
}