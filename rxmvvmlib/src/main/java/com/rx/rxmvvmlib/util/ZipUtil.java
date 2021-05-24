package com.rx.rxmvvmlib.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by wuwei
 * 2021/5/23
 * 佛祖保佑       永无BUG
 */
public class ZipUtil {
    public static final String TAG = "ZipUtil";

    /**
     * 压缩
     *
     * @param srcFilePath 待压缩的目录或文件路径
     * @param tarFilePath 压缩后的文件路径
     */
    public static void compress(String srcFilePath, String tarFilePath) throws IOException {
        File folder = new File(srcFilePath);
        if (folder.exists()) {
            List<File> fileList = getSubFiles(new File(srcFilePath));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tarFilePath));
            ZipEntry ze = null;
            File file = null;
            byte[] buf = new byte[1024];
            int len = 0;
            String fileName = null;
            for (int i = 0; i < fileList.size(); i++) {
                file = fileList.get(i);
                //回调外部，用于更新进度条
                if (file.getPath().length() > srcFilePath.length()) {
                    fileName = file.getPath().substring(srcFilePath.length() + 1);
                } else {
                    fileName = file.getName();
                }
                //文件实体
                ze = new ZipEntry(fileName);
                ze.setSize(file.length());
                ze.setTime(file.lastModified());
                //将实体加入压缩包中
                zos.putNextEntry(ze);
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                while ((len = is.read(buf, 0, 1024)) != -1) {
                    zos.write(buf, 0, len);
                }
                is.close();
            }
            zos.close();
        } else {
            throw new IOException("this folder isnot exist!");
        }
    }


    /**
     * 取得指定目录下的所有文件列表，包括子目录.
     *
     * @param baseDir 指定的目录或文件,如果是文件就加入列表，目录就继续取下级子文件
     * @return 文件列表
     */
    private static List<File> getSubFiles(File baseDir) {
        List<File> files = new ArrayList<File>();
        if (!baseDir.isDirectory()) {
            files.add(baseDir);
        } else {
            File[] tmp = baseDir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isFile()) {
                    files.add(tmp[i]);
                }
                if (tmp[i].isDirectory()) {
                    files.addAll(getSubFiles(tmp[i]));
                }
            }
        }
        return files;
    }

    /**
     * 解压zip到指定的路径
     *
     * @param zipFileString ZIP的名称
     * @param outPathString 要解压缩路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                Log.e(TAG, outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()) {
                    Log.e(TAG, "Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * 解压assets的zip压缩文件到指定目录
     *
     * @param context         上下文对象
     * @param assetName       压缩文件名
     * @param outputDirectory 输出目录
     * @param isReWrite       是否覆盖
     * @throws IOException
     */
    public static void unZipFromAssets(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    public static boolean assetsIsExit(Context c, String pt) {
        AssetManager am = c.getAssets();
        try {
            String[] names = am.list("");
            for (int i = 0; i < names.length; i++) {
                //Log.d("FILES",names[i]);
                if (names[i].equals(pt.trim())) {
                    System.out.println(pt + "文件存在！！！！");
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("不存在！");
        return false;
    }
}
