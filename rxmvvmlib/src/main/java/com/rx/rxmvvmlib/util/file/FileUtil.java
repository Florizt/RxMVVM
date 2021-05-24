package com.rx.rxmvvmlib.util.file;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wuwei
 * 2018/10/27
 * 佛祖保佑       永无BUG
 */
public class FileUtil {
    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_AUDIO = 3;
    public final static int TYPE_DOWNLOAD = 4;
    public final static int TYPE_DOCUMENT = 5;

    public static final String POST_IMAGE = ".jpeg";
    public static final String POST_VIDEO = ".mp4";
    public static final String POST_AUDIO = ".mp3";

    public static File createFile(Context context, int type, String fileName, String format) {
        fileName = TextUtils.isEmpty(fileName) ? String.valueOf(System.currentTimeMillis()) : fileName;
        File tmpFile = null;
        String suffixType;
        try {
            switch (type) {
                case FileUtil.TYPE_VIDEO:
                    tmpFile = new File(createDir(context, type),
                            (fileName.lastIndexOf(".") > 0 && fileName.lastIndexOf(".") + 1 < fileName.length()) ? fileName : fileName + POST_VIDEO);
                    break;
                case FileUtil.TYPE_AUDIO:
                    tmpFile = new File(createDir(context, type),
                            (fileName.lastIndexOf(".") > 0 && fileName.lastIndexOf(".") + 1 < fileName.length()) ? fileName : fileName + POST_AUDIO);
                    break;
                case FileUtil.TYPE_IMAGE:
                    tmpFile = new File(createDir(context, type),
                            (fileName.lastIndexOf(".") > 0 && fileName.lastIndexOf(".") + 1 < fileName.length()) ? fileName : fileName + POST_IMAGE);
                    break;
                case FileUtil.TYPE_DOWNLOAD:
                default:
                    suffixType = TextUtils.isEmpty(format) ? POST_IMAGE : format;
                    tmpFile = new File(createDir(context, type),
                            (fileName.lastIndexOf(".") > 0 && fileName.lastIndexOf(".") + 1 < fileName.length()) ? fileName : fileName + suffixType);
                    break;
            }
            if (tmpFile != null && !tmpFile.exists() && tmpFile.createNewFile()) {

            }
        } catch (Exception e) {

        }
        return tmpFile;
    }

    public static File createDir(Context context, int type) {
        File rootDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rootDir = getRootDirFile(context, type);
        } else {
            String state = Environment.getExternalStorageState();
            rootDir = state.equals(Environment.MEDIA_MOUNTED) ?
                    Environment.getExternalStorageDirectory() : context.getCacheDir();
        }
        if (rootDir != null && !rootDir.exists() && rootDir.mkdirs()) {
        }

        File folderDir = new File(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? rootDir.getAbsolutePath() : rootDir.getAbsolutePath() + getParentPath(context, type));

        if (folderDir != null && !folderDir.exists() && folderDir.mkdirs()) {
        }

        return folderDir;
    }

    public static File getRootDirFile(Context context, int type) {
        switch (type) {
            case FileUtil.TYPE_VIDEO:
                return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            case FileUtil.TYPE_AUDIO:
                return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            case FileUtil.TYPE_IMAGE:
                return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            case FileUtil.TYPE_DOWNLOAD:
                return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            case FileUtil.TYPE_DOCUMENT:
                return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            default:
                return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
    }

    public static String getParentPath(Context context, int type) {
        switch (type) {
            case FileUtil.TYPE_VIDEO:
                return "/" + context.getPackageName() + "/video/";
            case FileUtil.TYPE_AUDIO:
                return "/" + context.getPackageName() + "/audio/";
            case FileUtil.TYPE_IMAGE:
                return "/" + context.getPackageName() + "/image/";
            case FileUtil.TYPE_DOWNLOAD:
                return "/" + context.getPackageName() + "/download/";
            case FileUtil.TYPE_DOCUMENT:
                return "/" + context.getPackageName() + "/document/";
            default:
                return "/" + context.getPackageName() + "/download/";
        }
    }

    public static boolean copyFile(String oldPathName, String newPathName) {
        try {
            File oldFile = new File(oldPathName);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            FileInputStream fileInputStream = new FileInputStream(oldPathName);    //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] File2Bytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File byte2File(byte[] buf, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
