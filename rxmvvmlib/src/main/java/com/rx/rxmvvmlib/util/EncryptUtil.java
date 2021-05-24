package com.rx.rxmvvmlib.util;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wuwei
 * 2018/8/28
 * 佛祖保佑       永无BUG
 */
public class EncryptUtil {

    //-----------------------------------------------3DES----------------------------------------------------

    /**
     * 加密
     *
     * @param data 加密数据
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(byte[] data, String psw) throws Exception {
        // 恢复密钥
        SecretKey secretKey = new SecretKeySpec(build3Deskey(psw.getBytes()), "DESede");
        // Cipher完成加密
        Cipher cipher = Cipher.getInstance("DESede");
        // cipher初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypt = cipher.doFinal(data);
        //转码
        return new String(Base64.encode(encrypt, Base64.DEFAULT), "UTF-8");
    }

    /**
     * 解密
     *
     * @param data 加密后的字符串
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String data, String psw) throws Exception {
        // 恢复密钥
        SecretKey secretKey = new SecretKeySpec(build3Deskey(psw.getBytes()), "DESede");
        // Cipher完成解密
        Cipher cipher = Cipher.getInstance("DESede");
        // 初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        //转码
        byte[] bytes = Base64.decode(data.getBytes("UTF-8"), Base64.DEFAULT);
        //解密
        byte[] plain = cipher.doFinal(bytes);
        //解密结果转码
        return new String(plain, "utf-8");
    }

    public static byte[] build3Deskey(byte[] temp) throws Exception {
        byte[] key = new byte[24];
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
    //---------------------------------------------------------------------------------------------------


    //-----------------------------------------------MD5----------------------------------------------------
    public static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }

    public static String fileToMD5(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase(Locale.ENGLISH);//转为大写
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
    //---------------------------------------------------------------------------------------------------
}
