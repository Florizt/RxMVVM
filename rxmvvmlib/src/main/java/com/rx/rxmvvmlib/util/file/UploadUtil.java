package com.rx.rxmvvmlib.util.file;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by wuwei
 * 2018/6/8
 * 佛祖保佑       永无BUG
 */
public class UploadUtil {

    public static void upload(final Context context,
                       final String url,
                       final File file,
                       final UploadListener listener) {
        upload(context, url, false, null, file, listener);
    }

    public static void upload(final Context context,
                       final String url, final boolean setSSL, final String hostName,
                       final File file,
                       final UploadListener listener) {
        try {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            new RequestBody() {
                                private BufferedSink bufferedSink;

                                @Override
                                public MediaType contentType() {
                                    return MediaType.parse("multipart/form-data");
                                }

                                @Override
                                public long contentLength() throws IOException {
                                    return file.length();
                                }

                                @Override
                                public void writeTo(BufferedSink sink) throws IOException {

                                    if (bufferedSink == null) {
                                        bufferedSink = Okio.buffer(sink(sink));
                                    }
                                    //写入
                                    writeTo(bufferedSink);
                                    //刷新
                                    bufferedSink.flush();
                                }

                                private Sink sink(BufferedSink sink) {

                                    return new ForwardingSink(sink) {
                                        long bytesWritten = 0L;
                                        long contentLength = 0L;

                                        @Override
                                        public void write(Buffer source, long byteCount) throws IOException {
                                            super.write(source, byteCount);
                                            if (contentLength == 0) {
                                                contentLength = contentLength();
                                            }
                                            bytesWritten += byteCount;
                                            //回调
                                            if (listener != null) {
                                                int progress = (int) (bytesWritten * 1.0f / contentLength * 100);
                                                listener.onUploading(progress);
                                            }
                                        }
                                    };
                                }
                            })
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(multipartBody)
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
                    // 上传失败
                    if (listener != null) {
                        listener.onUploadFailed(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null) {
                        if (response != null) {
                            listener.onUploadSuccess();
                        }
                    }
                }
            });
        } catch (Exception e) {
            // 上传失败
            if (listener != null) {
                listener.onUploadFailed(e.getMessage());
            }
        }
    }

    public static void setSSL(OkHttpClient okHttpClient, final String hostName) {
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

    public interface UploadListener {
        /**
         * 上传成功
         */
        void onUploadSuccess();

        /**
         * @param progress 上传进度
         */
        void onUploading(int progress);

        /**
         * 上传失败
         */
        void onUploadFailed(String msg);
    }

}
