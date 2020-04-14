package com.rx.rxmvvmlib.util;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public class EncryptHMACUtil {
    public static final String[] array = {":", "/", "?", "#", "[", "]", "@", "!", "$", " ",
            "&", "'", "(", ")", "*", "+", ",", ";", "\"",
            "<", ">", "{", "}", "|", "\\", "^", "~", "`",};
    //%3A %2F %3F %23 %5B %5D %40 %21 %24 %20 %26 %27 %28 %29 %2A %2B %2C %3B %3D %22 %3C %3E %25 %7B %7D %7C %5C %5E %7E %60
    public static final String[] urlArray = {"%3A", "%2F", "%3F", "%23", "%5B", "%5D", "%40", "%21", "%24", "%20",
            "%26", "%27", "%28", "%29", "%2A", "%2B", "%2C", "%3B", "%22",
            "%3C", "%3E", "%7B", "%7D", "%7C", "%5C", "%5E", "%7E", "%60"};

    public static String encodeUrl(String url) {
        for (int i = 0; i < array.length; i++) {
            if (url.contains(array[i])) {
                url = url.replace(array[i], urlArray[i]);
            }
        }
        return url;
    }

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data 被签名的字符串
     * @param key  密钥
     * @return 加密后的字符串
     */
    public static String encodeHMAC(String key, String data) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            result = mac.doFinal(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return bytesToHexString(result);
        } else {
            return null;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }
}
