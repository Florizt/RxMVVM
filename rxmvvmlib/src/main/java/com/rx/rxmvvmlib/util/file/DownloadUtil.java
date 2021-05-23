package com.example.test.file;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wuwei
 * 2018/6/8
 * 佛祖保佑       永无BUG
 */
public class DownloadUtil {

    public void download(final Context context,
                         final String url,
                         final File file,
                         final DownloadListener listener) {
        download(context, url, false, null, file, listener);
    }

    public void download(final Context context,
                         final String url, final boolean setSSL, final String hostName,
                         final File file,
                         final DownloadListener listener) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();
            if (setSSL) {
                setSSL(okHttpClient, hostName);
            }
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 下载失败
                    if (listener != null) {
                        listener.onDownloadFailed(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        if (file.exists()) {
                            file.delete();
                        }
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
                            if (listener != null) {
                                listener.onDownloading(progress);
                            }
                        }
                        fos.flush();
                        // 下载完成
                        if (listener != null) {
                            listener.onDownloadSuccess(file);
                        }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onDownloadFailed(e.getMessage());
                        }
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                            if (listener != null) {
                                listener.onDownloadFailed(e.getMessage());
                            }
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                            if (listener != null) {
                                listener.onDownloadFailed(e.getMessage());
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            if (listener != null) {
                listener.onDownloadFailed(e.getMessage());
            }
        }
    }

    private void setSSL(OkHttpClient okHttpClient, final String hostName) {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

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
                return TextUtils.equals(hostName, hostname);
            }
        };

        String workerClassName = "okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(okHttpClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(okHttpClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface DownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String msg);
    }
}
