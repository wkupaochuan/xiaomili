package tools.media_record;


import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;

public class MediaRecordFunc {


    // 单例
    private static MediaRecordFunc instance;

    // 是否正在录音
    private boolean isRecording = false;

    // 录音组件
    private MediaRecorder mediaRecorder;

    // 录音文件
    private static String filePath;

    /**
     * 单例模式
     * @return
     */
    public static MediaRecordFunc getInstance()
    {
        if(null == instance)
        {
            instance = new MediaRecordFunc();
        }
        return instance;
    }


    /**
     * 构造
     */
    public MediaRecordFunc()
    {
        this.mediaRecorder = new MediaRecorder();

        // 设置麦克风为输入
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        /**
         * 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
          */
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

        /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // 设置输出文件
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        filePath += "/FinalAudio.amr";
        this.mediaRecorder.setOutputFile(filePath);
    }


    /**
     * 开始录音
     */
    public void startRecord()
    {
        if(this.isRecording)
        {
            return;
        }

        if(null == instance)
        {
            instance = new MediaRecordFunc();
        }

        try{
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();

            // 设置正在录制状态
            this.isRecording = true;
        }
        catch(IOException e)
        {
        }
    }


    /**
     * 停止录音
     */
    public String stopRecord()
    {
        if(null != this.mediaRecorder)
        {
            this.isRecording = false;
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
        }
        return filePath;
    }



}
