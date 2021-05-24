package com.rx.rxmvvmlib.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * Created by wuwei
 * 2021/5/23
 * 佛祖保佑       永无BUG
 */
public class CameraUtil {
    public final static String MIME_TYPE_IMAGE = "image/jpeg";
    public final static String MIME_TYPE_VIDEO = "video/mp4";
    public final static String MIME_TYPE_AUDIO = "audio/mpeg";

    public static void openAlbum(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * start to camera、preview、crop
     */
    public static void startOpenCameraPicture(Context context, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageUri = createImagePathUri(context, file.getName());
            } else {
                imageUri = parUri(context, file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }

    /**
     * start to camera、video
     */
    public static void startOpenCameraVideo(Context context, File file, int requestCode, int recordVideoSecond, int videoQuality) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageUri = createImageVideoUri(context, file.getName());
            } else {
                imageUri = parUri(context, file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, recordVideoSecond);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, videoQuality);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }

    /**
     * start to camera audio
     */
    public static void startOpenCameraAudio(Context context, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageUri = createImageAudioUri(context, file.getName());
            } else {
                imageUri = parUri(context, file);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }

    public static void startCrop(Context context, File file, int aspectX, int aspectY,
                                 int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    public static Uri parUri(Context context, File file) {
        Uri imageUri;
        String authority = context.getPackageName() + ".fileprovider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(context, authority, file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        return imageUri;
    }

    public static Uri createImagePathUri(Context context, String fileName) {
        final Uri[] imageFilePath = {null};
        String status = Environment.getExternalStorageState();
        String time = String.valueOf(System.currentTimeMillis());
        String imageName = TextUtils.isEmpty(fileName) ? time : fileName;
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_IMAGE);
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

    public static Uri createImageVideoUri(Context context, String fileName) {
        final Uri[] imageFilePath = {null};
        String status = Environment.getExternalStorageState();
        String time = String.valueOf(System.currentTimeMillis());
        String imageName = TextUtils.isEmpty(fileName) ? time : fileName;
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_VIDEO);
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

    public static Uri createImageAudioUri(Context context, String fileName) {
        final Uri[] imageFilePath = {null};
        String status = Environment.getExternalStorageState();
        String time = String.valueOf(System.currentTimeMillis());
        String imageName = TextUtils.isEmpty(fileName) ? time : fileName;
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_AUDIO);
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
}
