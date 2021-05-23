package com.example.test.file;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wuwei
 * 2021/5/23
 * 佛祖保佑       永无BUG
 */
public class RecordAudioUtil {
    private File file;
    private AudioRecordListener listener;
    private MediaRecorder mediaRecorder;
    private boolean isRunning = false;
    private Timer timer;

    /**
     * 采样率
     * RAW_AMR和AMR_NB：8KHZ
     * AMR_WB：16KHZ
     *
     * 头文件长度
     * RAW_AMR和AMR_NB：6个字节
     * AMR_WB：9个字节
     */

    /**
     * 开始录音
     *
     * @return 开始成功/失败
     */
    public boolean startRecord(Context context, File file, int maxDuration, int canUseSize,
                               AudioRecordListener listener) {
        this.file = file;
        this.listener = listener;
        if (isRunning) {
            return false;
        }
        if (!hasSdcard()) {
            Toast.makeText(context, "请先插入SD卡(存储卡)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isSDCanUseSize(canUseSize)) {
            Toast.makeText(context, "内存已经不足50M了，请先清理手机空间", Toast.LENGTH_SHORT).show();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 在setOutputFormat之前
        mediaRecorder.setOutputFile(file.getAbsolutePath());//设置输出路径【会直接保存】

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(16000);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(8000); // 96 kHz is a very high sample rate, and is not guaranteed to be supported. I suggest that you try a common sample rate <= 48 kHz (e.g. 48000, 44100, 22050, 16000, 8000).
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setMaxDuration(maxDuration);
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecord();
                }
            }
        });
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            startVoiceTimer();
        } catch (Exception e) {//未授权也会在这里抛出异常
            e.printStackTrace();
            Log.d("------------>录音", "mMediaRecorder报错:" + e.getMessage());
            if (listener != null) {
                listener.onRecordFailed(e.getMessage());
            }
            isRunning = false;
            return false;
        }
        isRunning = true;
        return true;
    }

    //获取声音分贝的timer
    private void startVoiceTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaRecorder != null) {
                    int voiceh = mediaRecorder.getMaxAmplitude();
                    double ratio = (double) voiceh / 100;
                    double db = 0;// 分贝
                    //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
                    //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
                    //同时，也可以配置灵敏度sensibility
                    if (ratio > 1) {
                        db = 20 * Math.log10(ratio);
                    }
                    if (listener != null) {
                        listener.onRecording(db);
                    }
                }
            }
        }, 100, 100);
    }

    /**
     * 停止录音
     * 【不对外提供是否保存的功能——始终保存】
     * isNeedSave 是否保存录音文件, true表示保存, false表示不保存
     */
    public void stopRecord() {
        if (!isRunning) {
            return;
        }
        if (mediaRecorder == null) {
            return;
        }
        if (timer != null) {
            timer.cancel();
        }

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRunning = false;

        if (listener != null) {
            listener.onRecordSuccess(file);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得sd卡剩余容量是否有50M，即可用大小
     *
     * @return
     */
    public static boolean isSDCanUseSize(int canUseSize) {
        if (!hasSdcard()) {
            return false;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long size = sf.getBlockSizeLong();//SD卡的单位大小
        long total = sf.getBlockCountLong();//总数量
        long available = sf.getAvailableBlocksLong();//可使用的数量
        DecimalFormat df = new DecimalFormat();
        df.setGroupingSize(3);//每3位分为一组
        if (size * available / 1024 / 1024 < canUseSize) {
            return false;
        }
        return true;
    }

    public interface AudioRecordListener {
        void onRecordSuccess(File file);

        void onRecordFailed(String msg);

        void onRecording(double db);
    }

    /**
     * 需求:将两个amr格式音频文件合并为1个
     * 注意:amr格式的头文件为6个字节的长度
     *
     * @param files      各部分路径
     * @param unitedFile 合并后路径
     */
    public static void uniteAMRFile(List<File> files, File unitedFile) {
        try {
            FileOutputStream fos = new FileOutputStream(unitedFile);
            RandomAccessFile ra = null;
            for (int i = 0; i < files.size(); i++) {
                ra = new RandomAccessFile(files.get(i), "r");
                if (i != 0) {
                    ra.seek(6);
                }
                byte[] buffer = new byte[1024 * 8];
                int len = 0;
                while ((len = ra.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            ra.close();
            fos.close();
        } catch (Exception e) {
        }
    }
}
