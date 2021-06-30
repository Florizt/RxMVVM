package com.rx.rxmvvmlib.core.audio;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by wuwei
 * 2021/6/1
 * 佛祖保佑       永无BUG
 */
public class PCMToAAC {

    /**
     * pcm to aac
     *
     * @param pcmPath pcm文件路径
     * @param aacPath 目标aac文件路径
     */
    public static void startEncode(String pcmPath, String aacPath) {
        try {
            //参数对应-> mime type、采样率、声道数
            MediaFormat encodeFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, 16000, 1);
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, 64000);//比特率
            encodeFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            encodeFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 2048);//作用于inputBuffer的大小
            MediaCodec mediaEncode = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mediaEncode.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaEncode.start();

            readInputStream(pcmPath, aacPath, mediaEncode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readInputStream(String pcmPath, String aacPath, MediaCodec mediaCodec) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        BufferedOutputStream out = null;
        try {
            inputStream = new FileInputStream(pcmPath);
            outputStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(new FileOutputStream(aacPath, false));
            byte[] buffer = new byte[1024];
            while ((inputStream.read(buffer)) != -1) {
                dstAudioFormatFromPCM(buffer, out, mediaCodec);
                Log.e("wqs+readInputStream", "readInputStream: " + buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 编码PCM数据 得到AAC格式的音频文件
     */
    private static void dstAudioFormatFromPCM(byte[] pcmData, BufferedOutputStream outputStream, MediaCodec mediaCodec) {
        int inputIndex;
        ByteBuffer inputBuffer;
        int outputIndex;
        ByteBuffer outputBuffer;

        int outBitSize;
        int outPacketSize;
        byte[] PCMAudio;
        PCMAudio = pcmData;

        ByteBuffer[] encodeInputBuffers = mediaCodec.getInputBuffers();
        ByteBuffer[] encodeOutputBuffers = mediaCodec.getOutputBuffers();
        MediaCodec.BufferInfo encodeBufferInfo = new MediaCodec.BufferInfo();

        inputIndex = mediaCodec.dequeueInputBuffer(0);
        if (inputIndex != -1) {

            inputBuffer = encodeInputBuffers[inputIndex];
            inputBuffer.clear();
            inputBuffer.limit(PCMAudio.length);
            inputBuffer.put(PCMAudio);//PCM数据填充给inputBuffer
            mediaCodec.queueInputBuffer(inputIndex, 0, PCMAudio.length, 0, 0);//通知编码器 编码

            outputIndex = mediaCodec.dequeueOutputBuffer(encodeBufferInfo, 0);
            while (outputIndex > 0) {

                outBitSize = encodeBufferInfo.size;
                outPacketSize = outBitSize + 7;//7为ADT头部的大小
                outputBuffer = encodeOutputBuffers[outputIndex];//拿到输出Buffer
                outputBuffer.position(encodeBufferInfo.offset);
                outputBuffer.limit(encodeBufferInfo.offset + outBitSize);
                byte[] chunkAudio = new byte[outPacketSize];
                addADTStoPacket(chunkAudio, outPacketSize);//添加ADTS
                outputBuffer.get(chunkAudio, 7, outBitSize);//将编码得到的AAC数据 取出到byte[]中

                try {
                    //录制aac音频文件，保存在手机内存中
                    outputStream.write(chunkAudio, 0, chunkAudio.length);
                    outputStream.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                outputBuffer.position(encodeBufferInfo.offset);
                mediaCodec.releaseOutputBuffer(outputIndex, false);
                outputIndex = mediaCodec.dequeueOutputBuffer(encodeBufferInfo, 0);

            }

        }
    }

    /**
     * 添加ADTS头
     *
     * @param packet
     * @param packetLen
     */
    private static void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int freqIdx = 8; // 16KHz
        int chanCfg = 1; // CPE

        // fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF1;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;

    }
}
