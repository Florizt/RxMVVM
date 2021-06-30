package com.rx.rxmvvmlib.core.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.rx.rxmvvmlib.util.FileUtil;
import com.rx.rxmvvmlib.util.UriUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * 一个打开系统相册、系统相机拍照、录像的管理类
 * <p>
 * Created by wuwei
 * 2021/6/3
 * 佛祖保佑       永无BUG
 */
public class SystemCameraManager {
    private Context context;
    private ICameraCallBack iCameraCallBack;

    private static final int REQUESTCODE_ALBUM = 100;
    private static final int REQUESTCODE_IMAGE = 101;
    private static final int REQUESTCODE_VIDEO = 102;

    private File file;

    public SystemCameraManager(Context context, ICameraCallBack iCameraCallBack) {
        this.context = context;
        this.iCameraCallBack = iCameraCallBack;
    }

    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, REQUESTCODE_ALBUM);
        }
    }

    /**
     * start to camera、preview、crop
     */
    public void startOpenCameraPicture() {
        String fileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());
        if (!fileName.endsWith(".jpeg")) {
            fileName = fileName + ".jpeg";
        }
        file = new File(FileUtil.createDir(context, FileUtil.TYPE_IMAGE, ""), fileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            Uri imageUri = UriUtil.fileToUri(context, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, REQUESTCODE_IMAGE);
            }
        }
    }

    /**
     * start to camera、video
     */
    public void startOpenCameraVideo(int videoQuality) {
        startOpenCameraVideo(0, videoQuality);
    }

    /**
     * start to camera、video
     */
    public void startOpenCameraVideo(int recordVideoSecond, int videoQuality) {
        String fileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());
        if (!fileName.endsWith(".mp4")) {
            fileName = fileName + ".mp4";
        }
        file = new File(FileUtil.createDir(context, FileUtil.TYPE_VIDEO, ""), fileName);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            if (recordVideoSecond > 0) {
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, recordVideoSecond);
            }
            Uri imageUri = UriUtil.fileToUri(context, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, videoQuality);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, REQUESTCODE_VIDEO);
            }
        }
    }

    public void startCrop(Context context, File file, int aspectX, int aspectY,
                          int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(UriUtil.fileToUri(context, file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtil.fileToUri(context, file));
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_ALBUM) {
                if (iCameraCallBack != null) {
                    if (data != null && data.getData() != null) {
                        iCameraCallBack.onSuccess(new File(UriUtil.urlToPath(context, data.getData())));
                    }
                }
            } else if (requestCode == REQUESTCODE_IMAGE) {
                if (iCameraCallBack != null) {
                    if (file != null) {
                        iCameraCallBack.onSuccess(file);
                    }
                }
            } else if (requestCode == REQUESTCODE_VIDEO) {
                if (iCameraCallBack != null) {
                    if (file != null) {
                        iCameraCallBack.onSuccess(file);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (file != null && file.exists() && file.length() == 0) {
                file.delete();
            }
        }
    }

    public interface ICameraCallBack {
        void onSuccess(File file);
    }
}
