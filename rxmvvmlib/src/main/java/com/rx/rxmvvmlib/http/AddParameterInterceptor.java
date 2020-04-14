package com.rx.rxmvvmlib.http;


import com.rx.rxmvvmlib.util.EncryptHMACUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by wuwei
 * 2019/4/23
 * 佛祖保佑       永无BUG
 */
public class AddParameterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        StringBuilder sb = new StringBuilder();
        String priv_key = "oNg%Bj1KHQqFXpcfLd$ncN$Jv9!WaETM$rFYEBwFQvvp@B4hpHaA7ctYLJ2HTtY";
        Headers headers = request.headers();
        String reqId = headers.get("req-id");
        String appVer = headers.get("app-ver");
        String os = headers.get("os");
        String token = headers.get("token");
        String uid = headers.get("uid");
        String lang = headers.get("lang");
        sb.append("req-id").append("=").append(reqId).append("&")
                .append("os").append("=").append(os).append("&")
                .append("app-ver").append("=").append(appVer).append("&")
                .append("uid").append("=").append(uid).append("&")
                .append("token").append("=").append(token).append("&")
                .append("lang").append("=").append(lang).append("&");

        if (request.method().equals("GET") || request.method().equals("DELETE")) {
            HttpUrl url = request.url();
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < url.querySize(); i++) {
                String key = url.queryParameterName(i);
                String value = url.queryParameterValue(i);
                map.put(key, value);
            }
            if (map.size() > 0) {
                Map<String, String> newMap = sortMapByKey(map);
                Set<Map.Entry<String, String>> set = newMap.entrySet();
                Iterator<Map.Entry<String, String>> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    sb.append(entry.getKey())
                            .append("=")
                            .append(URLDecoder.decode(entry.getValue(), "utf-8"))
                            .append("&");
                }
            }

            String value = EncryptHMACUtil.encodeHMAC(priv_key,
                    sb.toString().substring(0, sb.toString().length() - 1)).toLowerCase();

            HttpUrl newUrl = url.newBuilder()
                    .addEncodedQueryParameter("checksum", value)
                    .build();
            request = request.newBuilder()
                    .url(newUrl).build();
        } else if (request.method().equals("PUT") || request.method().equals("POST")) {
            FormBody body = (FormBody) request.body();

            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < body.size(); i++) {
                String key = body.encodedName(i);
                String value = body.encodedValue(i);
                map.put(key, value);
            }
            if (map.size() > 0) {
                Map<String, String> newMap = sortMapByKey(map);
                Set<Map.Entry<String, String>> set = newMap.entrySet();
                Iterator<Map.Entry<String, String>> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    sb.append(entry.getKey())
                            .append("=")
                            .append(URLDecoder.decode(entry.getValue(), "utf-8"))
                            .append("&");
                }
            }
            String value = EncryptHMACUtil.encodeHMAC(priv_key, sb.toString().substring(0,
                    sb.toString().length() - 1)).toLowerCase();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("checksum", value);
            FormBody newBody = builder.build();

            String postBodyString = bodyToString(body) + "&" + bodyToString(newBody);

            if (request.method().equals("PUT")) {
                request = request.newBuilder().put(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
                        postBodyString)).build();
            } else if (request.method().equals("POST")) {
                request = request.newBuilder().post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
                        postBodyString)).build();
            }
        }
        return chain.proceed(request);
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}

//比较器类
class MapKeyComparator implements Comparator<String> {
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
