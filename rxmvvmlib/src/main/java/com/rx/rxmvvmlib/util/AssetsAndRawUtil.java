package com.rx.rxmvvmlib.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwei
 * 2021/5/23
 * 佛祖保佑       永无BUG
 */
public class AssetsAndRawUtil {
    public static List<String> readAsset(Context context, String fileName) {
        List<String> list = new ArrayList<>();
        try {
            //获取文件中的字节
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            //将字节转换为字符
            InputStreamReader isReader = new InputStreamReader(inputStream, "UTF-8");
            //使用bufferReader去读取内容
            BufferedReader reader = new BufferedReader(isReader);
            String out = "";
            while ((out = reader.readLine()) != null) {
                list.add(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> readRaw(Context context, int resId) {
        List<String> list = new ArrayList<>();
        try {
            //获取文件中的内容
            InputStream inputStream = context.getResources().openRawResource(resId);
            //将文件中的字节转换为字符
            InputStreamReader isReader = new InputStreamReader(inputStream, "UTF-8");
            //使用bufferReader去读取字符
            BufferedReader reader = new BufferedReader(isReader);
            String out = "";
            try {
                while ((out = reader.readLine()) != null) {
                    list.add(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return list;
    }
}
