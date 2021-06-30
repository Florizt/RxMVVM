package com.rx.rxmvvmlib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wuwei
 * 2021/5/23
 * 佛祖保佑       永无BUG
 */
public class RecordVideoUtil {
    public static final String TAG = "RecordVideoUtil";
    private Context context;
    private int viewWidth;
    private int viewHeight;

    SensorManager sm;

    private Camera camera;
    private int cameraId;

    private int sensorAngle;
    private boolean isRecorder = false;
    MediaRecorder mediaRecorder;

    public RecordVideoUtil(Context context, int viewWidth, int viewHeight){
        this.context = context;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public void startPreview(SurfaceHolder holder) {
        if (camera == null) {
            try {
                if (Camera.getNumberOfCameras() == 2) {
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                } else {
                    camera = Camera.open();
                }
                cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (camera != null && Build.VERSION.SDK_INT > 17) {
            try {
                this.camera.enableShutterSound(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (holder == null) {
            return;
        }

        if (sm == null) {
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager
                .SENSOR_DELAY_NORMAL);

        if (camera != null) {
            try {
                int cameraAngle = getCameraDisplayOrientation(context, cameraId);
                Camera.Parameters parameters = camera.getParameters();

                Camera.Size previewSize = getSize(parameters.getSupportedPreviewSizes(), viewWidth, viewHeight);
                Camera.Size pictureSize = getSize(parameters.getSupportedPictureSizes(), viewWidth, viewHeight);

                Log.i(TAG, "startPreview: " + previewSize.width + "  " + previewSize.height);

                parameters.setPreviewSize(previewSize.width, previewSize.height);
                parameters.setPictureSize(pictureSize.width, pictureSize.height);

                if (isSupportedFocusMode(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                if (isSupportedPictureFormats(parameters.getSupportedPictureFormats(), ImageFormat.JPEG)) {
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    parameters.setJpegQuality(100);
                }

                camera.setParameters(parameters);
                camera.setPreviewDisplay(holder);  //SurfaceView
                camera.setDisplayOrientation(cameraAngle);//浏览角度
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {

                    }
                }); //每一帧回调
                camera.startPreview();//启动浏览

                Log.i(TAG, "=== Start Preview ===");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPreview() {
        if (null != camera) {
            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                //这句要在stopPreview后执行，不然会卡顿或者花屏
                camera.setPreviewDisplay(null);
                Log.i(TAG, "=== Stop Preview ===");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture() {
        if (camera == null) {
            return;
        }
        int cameraAngle = getCameraDisplayOrientation(context, cameraId);
        int tempCurrentAngle = 0;
        switch (cameraAngle) {
            case 90:
                tempCurrentAngle = Math.abs(sensorAngle + cameraAngle) % 360;
                break;
            case 270:
                tempCurrentAngle = Math.abs(cameraAngle - sensorAngle);
                break;
        }
        final int currentAngle = tempCurrentAngle;
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    matrix.setRotate(currentAngle);
                } else if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    matrix.setRotate(360 - currentAngle);
                    matrix.postScale(-1, 1);
                }

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        });
    }

    public void startRecord(Surface surface, File file) {
        if (camera == null) {
            return;
        }
        camera.setPreviewCallback(null);
        final int nowAngle = (sensorAngle + 90) % 360;

        if (isRecorder) {
            return;
        }

        mediaRecorder = new MediaRecorder();
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        camera.setParameters(parameters);
        camera.unlock();
        mediaRecorder.reset();
        mediaRecorder.setCamera(camera);

        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setOutputFormat(mProfile.fileFormat);
        mediaRecorder.setVideoEncoder(mProfile.videoCodec);
        mediaRecorder.setAudioEncoder(mProfile.audioCodec);

        Camera.Size videoSize = getSize(parameters.getSupportedVideoSizes(), viewWidth, viewHeight);
        mediaRecorder.setVideoSize(videoSize.width, videoSize.height);

        Log.i(TAG, "startRecord: " + videoSize.width + "  " + videoSize.height);

        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
        mediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
        mediaRecorder.setAudioEncodingBitRate(mProfile.audioBitRate);
        mediaRecorder.setAudioChannels(mProfile.audioChannels);
        mediaRecorder.setAudioSamplingRate(mProfile.audioSampleRate);

        if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            //手机预览倒立的处理
            int cameraAngle = getCameraDisplayOrientation(context, cameraId);
            if (cameraAngle == 270) {
                //横屏
                if (nowAngle == 0) {
                    mediaRecorder.setOrientationHint(180);
                } else if (nowAngle == 270) {
                    mediaRecorder.setOrientationHint(270);
                } else {
                    mediaRecorder.setOrientationHint(90);
                }
            } else {
                if (nowAngle == 90) {
                    mediaRecorder.setOrientationHint(270);
                } else if (nowAngle == 270) {
                    mediaRecorder.setOrientationHint(90);
                } else {
                    mediaRecorder.setOrientationHint(nowAngle);
                }
            }
        } else  if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            mediaRecorder.setOrientationHint(nowAngle);
        }

        mediaRecorder.setPreviewDisplay(surface);

        mediaRecorder.setOutputFile(file.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecorder = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "startRecord Exception");
        }
    }

    public void stopRecord() {
        if (!isRecorder) {
            return;
        }
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);
            try {
                mediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
                mediaRecorder = null;
                mediaRecorder = new MediaRecorder();
            } finally {
                if (mediaRecorder != null) {
                    mediaRecorder.release();
                }
                mediaRecorder = null;
                isRecorder = false;
            }
            stopPreview();
        }
    }

    public void destroyCamera() {
        if (null != camera) {
            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                //这句要在stopPreview后执行，不然会卡顿或者花屏
                camera.setPreviewDisplay(null);
                camera.release();
                camera = null;
                sm.unregisterListener(sensorEventListener);
                Log.i(TAG, "=== Destroy Camera ===");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "=== Camera  Null===");
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                return;
            }
            float[] values = event.values;
            sensorAngle = getSensorAngle(values[0], values[1]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public int getCameraDisplayOrientation(Context context, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = wm.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public Camera.Size getSize(List<Camera.Size> data, int viewWidth, int viewHeight) {
        List<Camera.Size> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Camera.Size size = data.get(i);
            Log.i(TAG, "doStasasartPreview111: " + size.width + "  " + size.height);
            if (equalRate(size, 1f * viewHeight / viewWidth)) {
                list.add(size);
            }
        }

        List<Camera.Size> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).width <= viewHeight) {
                list2.add(list.get(i));
            }
        }

        Collections.sort(list2, comp);
        for (int i = 0; i < list2.size(); i++) {
            Log.i(TAG, "doStasasartPreview222: " + list2.get(i).width + "  " + list2.get(i).height);
        }

        Log.i(TAG, "doStasasartPreview333: " + list2.get(0).width + "  " + list2.get(0).height);
        return list2.get(0);
    }

    private boolean equalRate(Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        return Math.abs(r - rate) <= 0.2;
    }

    private static Comparator<Camera.Size> comp = new Comparator<Camera.Size>() {

        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            return o2.width - o1.width;
        }
    };

    public boolean isSupportedFocusMode(List<String> focusList, String focusMode) {
        for (int i = 0; i < focusList.size(); i++) {
            if (focusMode.equals(focusList.get(i))) {
                Log.i(TAG, "FocusMode supported " + focusMode);
                return true;
            }
        }
        Log.i(TAG, "FocusMode not supported " + focusMode);
        return false;
    }

    public boolean isSupportedPictureFormats(List<Integer> supportedPictureFormats, int jpeg) {
        for (int i = 0; i < supportedPictureFormats.size(); i++) {
            if (jpeg == supportedPictureFormats.get(i)) {
                Log.i(TAG, "Formats supported " + jpeg);
                return true;
            }
        }
        Log.i(TAG, "Formats not supported " + jpeg);
        return false;
    }

    public int getSensorAngle(float x, float y) {
        if (Math.abs(x) > Math.abs(y)) {
            /**
             * 横屏倾斜角度比较大
             */
            if (x > 4) {
                /**
                 * 左边倾斜
                 */
                return 270;
            } else if (x < -4) {
                /**
                 * 右边倾斜
                 */
                return 90;
            } else {
                /**
                 * 倾斜角度不够大
                 */
                return 0;
            }
        } else {
            if (y > 7) {
                /**
                 * 左边倾斜
                 */
                return 0;
            } else if (y < -7) {
                /**
                 * 右边倾斜
                 */
                return 180;
            } else {
                /**
                 * 倾斜角度不够大
                 */
                return 0;
            }
        }
    }
}
