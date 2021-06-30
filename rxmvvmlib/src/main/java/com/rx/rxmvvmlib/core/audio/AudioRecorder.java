package com.rx.rxmvvmlib.core.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import com.rx.rxmvvmlib.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wuwei
 * 2021/6/1
 * 佛祖保佑       永无BUG
 * <p>
 * 用于实现录音、暂停、继续、停止、播放
 */
public class AudioRecorder {
    private static AudioRecorder audioRecorder;

    //-------------------------------------------参数start-----------------------------------------------
    //音频输入-麦克风
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    /**
     * 采样率即采样频率，采样频率越高，能表现的频率范围就越大
     * 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     */
    private final static int AUDIO_SAMPLE_RATE = 16000;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;

    /**
     * 位深度也叫采样位深，音频的位深度决定动态范围
     * 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
     */
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
    ;
    //-------------------------------------------参数end-----------------------------------------------

    //-------------------------------------------实例start-----------------------------------------------
    //录音对象
    private AudioRecord audioRecord;

    /**
     * 播放声音
     * 一些必要的参数，需要和AudioRecord一一对应，否则声音会出错
     */
    private AudioTrack audioTrack;
    //-------------------------------------------实例end-----------------------------------------------

    //-------------------------------------------状态和文件start-----------------------------------------------
    //录音状态,默认未开始
    private AudioStatus status = AudioStatus.STATUS_NO_READY;

    private String tempDirPath = FileUtil.createDir(context, FileUtil.TYPE_AUDIO, "pcm-temp").getAbsolutePath();
    private String mergeDirPath = FileUtil.createDir(context, FileUtil.TYPE_AUDIO, "pcm-merge").getAbsolutePath();
    private String finalDirPath = FileUtil.createDir(context, FileUtil.TYPE_AUDIO, "pcm-final").getAbsolutePath();
    //合成文件
    private String originFileName;

    //录音分片文件集合
    private List<String> tempPCMFilePaths = new ArrayList<>();
    //-------------------------------------------状态和文件end-----------------------------------------------

    private static Context context;
    //用来回调，转码后的文件绝对路径
    private static IAudioCallback iAudioCallback;
    /**
     * 创建带有缓存的线程池
     * 当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
     * 如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     */
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private AudioRecorder() {
    }

    /**
     * 单例，双重检验
     *
     * @param iAudio 用于合成后回调
     * @return
     */
    public static AudioRecorder getInstance(Context context, IAudioCallback iAudio) {
        AudioRecorder.context = context.getApplicationContext();
        AudioRecorder.iAudioCallback = iAudio;
        if (audioRecorder == null) {
            synchronized (AudioRecord.class) {
                if (audioRecorder == null) {
                    audioRecorder = new AudioRecorder();
                }
            }
        }
        return audioRecorder;
    }

    /**
     * 创建默认的录音对象
     */
    public void createDefaultAudio() {
        originFileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());

        //创建AudioRecord
        audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSizeInBytes);
        status = AudioStatus.STATUS_READY;

        //创建AudioTrack
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        AudioFormat audioFormat = new AudioFormat.Builder().setSampleRate(AUDIO_SAMPLE_RATE)
                .setEncoding(AUDIO_ENCODING).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build();
        audioTrack = new AudioTrack(audioAttributes, audioFormat, bufferSizeInBytes,
                AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        if (status == AudioStatus.STATUS_NO_READY) {
            throw new IllegalStateException("请检查录音权限");
        }
        if (status == AudioStatus.STATUS_START) {
            throw new IllegalStateException("正在录音");
        }
        try {
            audioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cachedThreadPool.execute(this::writeDataToFile);
    }

    /**
     * 暂停录音
     */
    public void pauseRecord() {
        if (status != AudioStatus.STATUS_START) {
            throw new IllegalStateException("没有在录音");
        } else {
            status = AudioStatus.STATUS_PAUSE;
            try {
                audioRecord.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        if (status == AudioStatus.STATUS_NO_READY || status == AudioStatus.STATUS_READY) {
            throw new IllegalStateException("录音尚未开始");
        } else {
            status = AudioStatus.STATUS_STOP;
            try {
                audioRecord.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            release();
            merge();
        }
    }

    /**
     * 将音频信息写入文件
     */
    private void writeDataToFile() {
        FileOutputStream fos = null;
        int readSize = 0;
        try {
            // new一个byte数组用来存一些字节数据，大小为缓冲区大小
            byte[] audioData = new byte[bufferSizeInBytes];

            String currentFileName = originFileName;
            if (status == AudioStatus.STATUS_PAUSE) {
                //假如是暂停录音 将文件名后面加个数字,防止重名文件内容被覆盖
                currentFileName = currentFileName + "_" + tempPCMFilePaths.size();
            }
            if (!currentFileName.endsWith(".pcm")) {
                currentFileName = currentFileName + ".pcm";
            }

            File file = new File(tempDirPath, currentFileName);
            if (file.exists()) {
                file.delete();
            }
            tempPCMFilePaths.add(file.getAbsolutePath());
            // 建立一个可存取字节的文件
            fos = new FileOutputStream(file);

            //将录音状态设置成正在录音状态
            status = AudioStatus.STATUS_START;
            while (status == AudioStatus.STATUS_START) {
                readSize = audioRecord.read(audioData, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize && fos != null) {
                    try {
                        long v = 0;
                        short[] buffer = bytesToShort(audioData);
                        // 将 buffer 内容取出，进行平方和运算
                        for (int i = 0; i < buffer.length; i++) {
                            v += buffer[i] * buffer[i];
                        }
                        // 平方和除以数据总长度，得到音量大小。
                        double mean = v / (double) readSize;
                        int volume = (int) (10 * Math.log10(mean));
                        if (iAudioCallback != null) {
                            iAudioCallback.onDecibelChanging(volume);
                        }

                        fos.write(audioData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (iAudioCallback != null) {
                iAudioCallback.makeTemp(file);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();// 关闭写入流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private short[] bytesToShort(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
    }

    public void merge() {
        try {
            if (tempPCMFilePaths.size() > 0) {
                //将多个pcm文件合并再转为aac/.m4a
                pcmFilesToAACFile();
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * 将pcm合并成aac
     */
    private void pcmFilesToAACFile() {
        String mergeFileName = originFileName;
        if (!mergeFileName.endsWith(".pcm")) {
            mergeFileName = mergeFileName + ".pcm";
        }
        String mergeFilePath = new File(mergeDirPath, mergeFileName).getAbsolutePath();
        cachedThreadPool.execute(() -> {
            if (PCMMerge.merge(tempPCMFilePaths, mergeFilePath)) {

                clearAllTemp();

                String aacFileName = originFileName;
                if (!aacFileName.endsWith(".m4a")) {
                    aacFileName = aacFileName + ".m4a";
                }
                String aacFilePath = new File(finalDirPath, aacFileName).getAbsolutePath();
                PCMToAAC.startEncode(mergeFilePath, aacFilePath);

                File mergeFile = new File(mergeFilePath);
                if (mergeFile.exists()) {
                    mergeFile.delete();
                }

                //合成后回调
                if (iAudioCallback != null) {
                    iAudioCallback.makeFinal(new File(aacFilePath));
                }
            } else {
                throw new IllegalStateException("合成失败");
            }
        });
    }

    public void clearAllTemp() {
        for (int i = 0; i < tempPCMFilePaths.size(); i++) {
            File file = new File(tempPCMFilePaths.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 播放合成后的wav文件
     *
     * @param filePath 文件的绝对路径
     */
    public void play(final String filePath) {
        audioTrack.play();

        cachedThreadPool.execute(() -> {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[bufferSizeInBytes];
            while (fis != null) {
                try {
                    int readCount = fis.read(buffer);
                    if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                        continue;
                    }
                    if (readCount != 0 && readCount != -1) {
                        audioTrack.write(buffer, 0, readCount);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 释放资源
     */
    public void release() {
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
        status = AudioStatus.STATUS_NO_READY;
    }

    /**
     * 释放audioTrack
     */
    public void releaseAudioTrack() {
        if (audioTrack == null) {
            return;
        }
        if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
            audioTrack.stop();
        }
        audioTrack.release();
        audioTrack = null;
    }

    public interface IAudioCallback {
        void makeTemp(File tempPCMFile);

        void makeFinal(File finalPCMFile);

        void onDecibelChanging(int decibel);
    }

    /**
     * 获取录音对象的状态
     */
    public AudioStatus getStatus() {
        return status;
    }
}