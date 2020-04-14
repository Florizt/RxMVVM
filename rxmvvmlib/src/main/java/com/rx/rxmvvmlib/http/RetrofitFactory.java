package com.rx.rxmvvmlib.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rx.rxmvvmlib.BuildConfig;
import com.rx.rxmvvmlib.config.HttpConfig;
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
        if (BuildConfig.DEBUG) {
            sUrl = HttpConfig.HTTP_DEBUG_URL;
        } else {
            sUrl = HttpConfig.HTTP_RELEASE_URL;
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        sOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new AddParameterInterceptor())
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .cache(cache)
                .build();

        /*SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                    LogUtil.i("", "opkdhiuasds----" + chain[0] + "   " + authType);
                    if (chain == null) {
                        throw new IllegalArgumentException("checkServerTrusted:x509Certificate array isnull");
                    }

                    if (!(chain.length > 0)) {
                        throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
                    }

                    *//*if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
                        throw new CertificateException("checkServerTrusted: AuthType is not RSA");
                    }*//*

                    try {
                        chain[0].checkValidity();
                    }catch (Exception e){
                        throw new CertificateException("Certificate not valid");
                    }
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                LogUtil.i("", "opkiuasdskd0-----" + hostname);
                return hostname.contains("supapp.cn");
            }
        };

        String workerClassName = "okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(sOkHttpClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(sOkHttpClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    public static ApiService sApiService = new Retrofit.Builder()
            .baseUrl(sUrl)
            .addConverterFactory(GsonDConverterFactory.create(sGson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(sOkHttpClient)
            .build()
            .create(ApiService.class);
}
