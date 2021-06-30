package com.rx.rxmvvmlib.core.audio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by wuwei
 * 2021/6/1
 * 佛祖保佑       永无BUG
 * <p>
 * 用于多个pcm文件合并
 */
public class PCMMerge {

    public static boolean merge(List<String> filePathList, String destinationPath) {
        File[] file = new File[filePathList.size()];
        byte buffer[] = null;

        for (int i = 0; i < filePathList.size(); i++) {
            file[i] = new File(filePathList.get(i));
        }

        //先删除目标文件
        File destFile = new File(destinationPath);
        if (destFile.exists()) {
            destFile.delete();
        }

        //合成所有的pcm文件的数据，写到目标文件
        try {
            buffer = new byte[1024 * 4]; // Length of All Files, Total Size
            InputStream inStream = null;
            OutputStream ouStream = null;

            ouStream = new BufferedOutputStream(new FileOutputStream(destinationPath));
            for (int j = 0; j < filePathList.size(); j++) {
                inStream = new BufferedInputStream(new FileInputStream(file[j]));
                int size = inStream.read(buffer);
                while (size != -1) {
                    ouStream.write(buffer);
                    size = inStream.read(buffer);
                }
                inStream.close();
            }
            ouStream.close();
        } catch (IOException ioe) {
            ioe.getMessage();
            return false;
        }
        return true;
    }
}