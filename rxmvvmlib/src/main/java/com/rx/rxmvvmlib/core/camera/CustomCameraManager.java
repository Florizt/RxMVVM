package com.rx.rxmvvmlib.core.camera;

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

import com.rx.rxmvvmlib.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wuwei
 * 2021/6/4
 * 佛祖保佑       永无BUG
 */
public class CustomCameraManager {
    private static final String TAG = "CustomCameraManager";
    private Context context;
    private ICameraCallBack cameraCallBack;

    //录音状态,默认未开始
    private VideoStatus status = VideoStatus.STATUS_NO_READY;

    private String tempDirPath;
    private String mergeDirPath;
    //合成文件
    private String originFileName;

    //录音分片文件集合
    private List<String> tempFilePaths = new ArrayList<>();

    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    //相机
    private Camera camera;
    private Camera.Parameters parameters;

    //角度
    private SensorManager sm;
    private int sensorAngle;

    //预览
    private boolean isPreviewing = false;
    private SurfaceHolder holder;
    private int viewWidth;
    private int viewHeight;

    //录像
    private MediaRecorder mediaRecorder;

    //摄像头
    private int SELECTED_CAMERA = -1;
    private int CAMERA_POST_POSITION = -1;
    private int CAMERA_FRONT_POSITION = -1;


    public CustomCameraManager(Context context, ICameraCallBack cameraCallBack) {
        this.context = context;
        this.cameraCallBack = cameraCallBack;
        findAvailableCameras();
        SELECTED_CAMERA = CAMERA_POST_POSITION;

        tempDirPath = FileUtil.createDir(context, FileUtil.TYPE_VIDEO, "video-temp").getAbsolutePath();
        mergeDirPath = FileUtil.createDir(context, FileUtil.TYPE_VIDEO, "video-merge").getAbsolutePath();
        originFileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());
    }

    private void findAvailableCameras() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int cameraNum = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraNum; i++) {
            Camera.getCameraInfo(i, info);
            switch (info.facing) {
                case Camera.CameraInfo.CAMERA_FACING_FRONT:
                    CAMERA_FRONT_POSITION = info.facing;
                    break;
                case Camera.CameraInfo.CAMERA_FACING_BACK:
                    CAMERA_POST_POSITION = info.facing;
                    break;
            }
        }
    }

    public void doOpenCamera() {
        if (camera == null) {
            openCamera(SELECTED_CAMERA);
        }
        status = VideoStatus.STATUS_READY;
        Log.i(TAG, "doOpenCamera----------: cameraOpened");
        cameraCallBack.cameraOpened();
    }

    private synchronized void openCamera(int id) {
        try {
            this.camera = Camera.open(id);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        if (Build.VERSION.SDK_INT > 17 && this.camera != null) {
            try {
                this.camera.enableShutterSound(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doStartPreview(SurfaceHolder holder, int viewWidth, int viewHeight) {
        if (isPreviewing) {
            Log.i(TAG, "doStartPreview---------: isPreviewing");
        }
        if (holder == null) {
            return;
        }
        this.holder = holder;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        if (camera != null) {
            try {
                parameters = camera.getParameters();
                Camera.Size previewSize = getSize(parameters.getSupportedPreviewSizes(), viewWidth, viewHeight);
                Camera.Size pictureSize = getSize(parameters.getSupportedPictureSizes(), viewWidth, viewHeight);

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
                parameters = camera.getParameters();
                camera.setPreviewDisplay(holder);  //SurfaceView
                int cameraAngle = getCameraDisplayOrientation(context, SELECTED_CAMERA);
                camera.setDisplayOrientation(cameraAngle);//浏览角度
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {

                    }
                }); //每一帧回调
                camera.startPreview();//启动浏览
                isPreviewing = true;
                Log.i(TAG, "=== Start Preview ===");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture() {
        if (camera == null) {
            return;
        }
        int cameraAngle = getCameraDisplayOrientation(context, SELECTED_CAMERA);
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
                if (SELECTED_CAMERA == CAMERA_POST_POSITION) {
                    matrix.setRotate(currentAngle);
                } else if (SELECTED_CAMERA == CAMERA_FRONT_POSITION) {
                    matrix.setRotate(360 - currentAngle);
                    matrix.postScale(-1, 1);
                }

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (cameraCallBack != null) {
                    if (currentAngle == 90 || currentAngle == 270) {
                        cameraCallBack.takePictureSuccess(bitmap, true);
                    } else {
                        cameraCallBack.takePictureSuccess(bitmap, false);
                    }
                }
            }
        });
    }

    public void startRecord(Surface surface) {
        camera.setPreviewCallback(null);
        final int nowAngle = (sensorAngle + 90) % 360;

        if (status == VideoStatus.STATUS_START) {
            return;
        }

        if (camera == null) {
            openCamera(SELECTED_CAMERA);
        }

        if (parameters == null) {
            parameters = camera.getParameters();
        }

        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }

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

        int cameraAngle = getCameraDisplayOrientation(context, SELECTED_CAMERA);
        if (SELECTED_CAMERA == CAMERA_FRONT_POSITION) {
            //手机预览倒立的处理
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
        } else {
            mediaRecorder.setOrientationHint(nowAngle);
        }

        mediaRecorder.setPreviewDisplay(surface);

        String currentFileName = originFileName;
        if (status == VideoStatus.STATUS_PAUSE) {
            //假如是暂停录像 将文件名后面加个数字,防止重名文件内容被覆盖
            currentFileName = currentFileName + "_" + tempFilePaths.size();
        }
        if (!currentFileName.endsWith(".mp4")) {
            currentFileName = currentFileName + ".mp4";
        }

        File file = new File(tempDirPath, currentFileName);
        if (file.exists()) {
            file.delete();
        }
        tempFilePaths.add(file.getAbsolutePath());
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            status = VideoStatus.STATUS_START;
        } catch (Exception e) {
            e.printStackTrace();
            if (file != null && file.exists() && file.length() == 0) {
                file.delete();
            }
            Log.i(TAG, "startRecord Exception");
        }
    }

    public void clearAllTemp() {
        for (int i = 0; i < tempFilePaths.size(); i++) {
            File file = new File(tempFilePaths.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 暂停录音
     */
    public void pauseRecord() {
        if (status != VideoStatus.STATUS_START) {
            throw new IllegalStateException("没有在录像");
        } else {
            status = VideoStatus.STATUS_PAUSE;
            doStopRecord();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        if (status == VideoStatus.STATUS_NO_READY || status == VideoStatus.STATUS_READY) {
            throw new IllegalStateException("录像尚未开始");
        } else {
            status = VideoStatus.STATUS_STOP;
            doStopRecord();
            status = VideoStatus.STATUS_NO_READY;
            merge();
        }
    }

    public void merge() {
        if (tempFilePaths.size() > 0) {
            String mergeFileName = originFileName;
            if (!mergeFileName.endsWith(".mp4")) {
                mergeFileName = mergeFileName + ".mp4";
            }
            String mergeFilePath = new File(mergeDirPath, mergeFileName).getAbsolutePath();
            cachedThreadPool.execute(() -> {
                Mp4ParserMerge.mergeVideos(tempFilePaths, mergeFilePath);
                clearAllTemp();
                if (cameraCallBack != null) {
                    cameraCallBack.recordSuccess(new File(mergeFilePath));
                }
            });
        }
    }

    private void doStopRecord() {
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
            }
            doStopPreview();
        }
    }

    /**
     * 停止预览
     */
    public void doStopPreview() {
        if (null != camera) {
            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                //这句要在stopPreview后执行，不然会卡顿或者花屏
                camera.setPreviewDisplay(null);
                isPreviewing = false;
                Log.i(TAG, "=== Stop Preview ===");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁Camera
     */
    void doDestroyCamera() {
        if (null != camera) {
            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                //这句要在stopPreview后执行，不然会卡顿或者花屏
                camera.setPreviewDisplay(null);
                holder = null;
                isPreviewing = false;
                camera.release();
                camera = null;
                Log.i(TAG, "=== Destroy Camera ===");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "=== Camera  Null===");
        }
    }

    void registerSensorManager(Context context) {
        if (sm == null) {
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager
                .SENSOR_DELAY_NORMAL);
    }

    void unregisterSensorManager(Context context) {
        if (sm == null) {
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        sm.unregisterListener(sensorEventListener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                return;
            }
            float[] values = event.values;
            sensorAngle = getSensorAngle(values[0], values[1]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

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

    public interface ICameraCallBack {
        void cameraOpened();

        void takePictureSuccess(Bitmap bitmap, boolean isVertical);

        void recordSuccess(File file);
    }
}
