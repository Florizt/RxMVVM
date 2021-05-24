package com.rx.rxmvvmlib.mode.remote.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rx.rxmvvmlib.RxMVVMInit;
import com.rx.rxmvvmlib.util.UIUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public class RetrofitFactory {
    public static String url;
    private static OkHttpClient okHttpClient;
    private static File cacheFile = new File(UIUtil.getContext().getCacheDir(), "cache");
    private static Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final Gson gson = new GsonBuilder()
            .setDateFormat(pattern)
            .create();

    static {
        try {
            url = RxMVVMInit.getConfig().getHttpBaseUrl();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(15000, TimeUnit.MILLISECONDS)
                    .writeTimeout(15000, TimeUnit.MILLISECONDS)
                    .connectTimeout(15000, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .cache(cache);
            if (RxMVVMInit.getConfig().getInterceptors() != null
                    && RxMVVMInit.getConfig().getInterceptors().size() > 0) {
                for (Interceptor interceptor : RxMVVMInit.getConfig().getInterceptors()) {
                    builder.addInterceptor(interceptor);
                }
            } else {
                builder.addInterceptor(new JsonInterceptor());
                builder.addInterceptor(new LoggingInterceptor());
            }
            okHttpClient = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <S> S create(Class<S> clazz) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonDConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(clazz);
    }
}
