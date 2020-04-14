package com.rx.rxmvvmlib.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.rx.rxmvvmlib.config.FileConfig;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import androidx.core.content.FileProvider;

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

    public final static String MIME_TYPE_IMAGE = "image/jpeg";
    public final static String MIME_TYPE_VIDEO = "video/mp4";
    public final static String MIME_TYPE_AUDIO = "audio/mpeg";

    public static boolean saveImageToFile(Context context, Bitmap bmp, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            boolean compress = bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return compress;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
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

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);    //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
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

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    public static Uri parUri(Context context, File cameraFile) {
        Uri imageUri;
        String authority = context.getPackageName() + ".fileprovider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(context, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @param fileName 不包含后缀
     * @return
     */
    public static Uri createImagePathUri(Context context, String fileName) {
        final Uri[] imageFilePath = {null};
        String status = Environment.getExternalStorageState();
        String time = String.valueOf(System.currentTimeMillis());
        String imageName = TextUtils.isEmpty(fileName) ? time : fileName;
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, FileUtil.MIME_TYPE_IMAGE);
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            imageFilePath[0] = context.getContentResolver()
                    .insert(MediaStore.Files.getContentUri("external"), values);
        } else {
            imageFilePath[0] = context.getContentResolver()
                    .insert(MediaStore.Files.getContentUri("internal"), values);
        }
        return imageFilePath[0];
    }

    /**
     * 创建一条视频地址uri,用于保存录制的视频
     *
     * @param context
     * @param fileName 不包含后缀
     * @return
     */
    public static Uri createImageVideoUri(Context context, String fileName) {
        final Uri[] imageFilePath = {null};
        String status = Environment.getExternalStorageState();
        String time = String.valueOf(System.currentTimeMillis());
        String imageName = TextUtils.isEmpty(fileName) ? time : fileName;
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, FileUtil.MIME_TYPE_VIDEO);
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            imageFilePath[0] = context.getContentResolver()
                    .insert(MediaStore.Files.getContentUri("external"), values);
        } else {
            imageFilePath[0] = context.getContentResolver()
                    .insert(MediaStore.Files.getContentUri("internal"), values);
        }
        return imageFilePath[0];
    }


    /**
     * 创建文件
     *
     * @param context
     * @param type
     * @param fileName
     * @return
     */
    public static File createFile(Context context, int type, String fileName, String format) {
        fileName = TextUtils.isEmpty(fileName) ? String.valueOf(System.currentTimeMillis()) : fileName;
        File tmpFile;
        String suffixType;
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
        return tmpFile;
    }

    public static File createDir(Context context, int type) {
        File rootDir;
        if (SystemUtils.checkedAndroid_Q()) {
            rootDir = getRootDirFile(context, type);
        } else {
            String state = Environment.getExternalStorageState();
            rootDir = state.equals(Environment.MEDIA_MOUNTED) ?
                    Environment.getExternalStorageDirectory() : context.getCacheDir();
        }
        if (rootDir != null && !rootDir.exists() && rootDir.mkdirs()) {
        }

        File folderDir = new File(SystemUtils.checkedAndroid_Q()
                ? rootDir.getAbsolutePath() : rootDir.getAbsolutePath() + getParentPath(type));

        if (folderDir != null && !folderDir.exists() && folderDir.mkdirs()) {
        }

        return folderDir;
    }

    /**
     * 文件根目录
     *
     * @param context
     * @param type
     * @return
     */
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


    /**
     * 内存卡目录下的媒体文件目录
     *
     * @param type
     * @return
     */
    public static String getParentPath(int type) {
        switch (type) {
            case FileUtil.TYPE_VIDEO:
                return FileConfig.VIDEO;
            case FileUtil.TYPE_AUDIO:
                return FileConfig.AUDIO;
            case FileUtil.TYPE_IMAGE:
                return FileConfig.IMAGE;
            case FileUtil.TYPE_DOWNLOAD:
                return FileConfig.DOWNLOAD;
            case FileUtil.TYPE_DOCUMENT:
                return FileConfig.DOCUMENT;
            default:
                return FileConfig.DOWNLOAD;
        }
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (SystemUtils.hasKitKat() && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    if (SystemUtils.checkedAndroid_Q()) {
                        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + split[1];
                    } else {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException ex) {
            LogUtil.i("sa", String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", ex.getMessage()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
