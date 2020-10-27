package com.rx.rxmvvmlib.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rx.rxmvvmlib.RxMVVMInitializer;
import com.rx.rxmvvmlib.http.api.ApiService;
import com.rx.rxmvvmlib.util.UIUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
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
    public static String sUrl;
    private static OkHttpClient sOkHttpClient;
    private static File cacheFile = new File(UIUtils.getContext().getCacheDir(), "cache");
    private static Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final Gson sGson = new GsonBuilder()
            .setDateFormat(pattern)
            .create();

    static {
        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            sUrl = RxMVVMInitializer.getInstance().getAppConfig().getHttpDebugUrl();
        } else {
            sUrl = RxMVVMInitializer.getInstance().getAppConfig().getHttpReleaseUrl();
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        sOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .cache(cache)
                .build();
    }

    public static ApiService sApiService = new Retrofit.Builder()
            .baseUrl(sUrl)
            .addConverterFactory(GsonDConverterFactory.create(sGson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(sOkHttpClient)
            .build()
            .create(ApiService.class);
}
