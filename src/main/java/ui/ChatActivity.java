package ui;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ai.welcome.R;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;

import api.ClientCallBack;
import api.chat.DownLoadFileTask;
import api.chat.UploadFile;
import constants.ConstantsCommon;
import model.chat.JsonMessage;
import service.chat.ChatConnectManager;
import tools.XMediaPlayer;


public class ChatActivity extends BaseActivity{

    private EditText tvMsg;

    private MediaRecorder mRecorder = null;

    //语音文件保存路径
    private String FileName = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        this.tvMsg = (EditText)this.findViewById(R.id.tv_sms);

        // 使用deviceid登陆(如果用户不存在，注册后登陆)
        ChatConnectManager.login(Welcome.deviceId);

        //设置sdcard的路径
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audiorecordtest.3gp";
    }


    public void sendMessage(View view)
    {
        String msg = this.tvMsg.getText().toString();

        ChatConnectManager.sendMessage(msg, "test1@iz255gm1qk6z/Spark 2.6.3");
    }



    /**
     * 发送多媒体消息
     * @param filePath
     */
    public void sendMediaMessage(String filePath)
    {
        JsonMessage jMsg = new JsonMessage();
        jMsg.messageType = JsonMessage.MSG_TYPE_VOICE;
        jMsg.file = filePath;
        String msgContent = JSON.toJSONString(jMsg);

        ChatConnectManager.sendMessage(msgContent, "test1@iz255gm1qk6z/Spark 2.6.3");
    }


    /**
     * 开始录音
     * @param view
     */
    public void startRecord(View view)
    {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(FileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }
        mRecorder.start();
    }


    /**
     * 结束录音
     * @param view
     */
    public void stopRecord(View view){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        // 播放录音
//        XMediaPlayer.play(this.FileName);

        // 上传文件到服务器
        this.upload(this.FileName);
    }



    /**
     * 上传文件
     * @param filePath
     */
    private void upload(String filePath)
    {

        UploadFile.uploadFile(filePath, new ClientCallBack() {

            // 请求成功
            public void onSuccess(Object data) {
                // 获取上传后的路径
                String fileUrl = (String) data;

                Log.e(ConstantsCommon.LOG_TAG, "文件路径" + fileUrl);

                ChatActivity.this.sendMediaMessage(fileUrl);
            }

            // 请求失败
            public void onFailure(String message) {
                Log.e(ConstantsCommon.LOG_TAG, message);
            }

        });

    }



    /**
     * 播放消息
     * @param strMsg
     */
    public static void playMsg(String strMsg)
    {
        Log.e(ConstantsCommon.LOG_TAG, "接收到消息:" + strMsg);
        JsonMessage jMsg = JsonMessage.parse(strMsg);
        String fileUrl = "http://toy-api.wkupaochuan.com/" + jMsg.file;
        String filePath = ChatActivity.getDownloadFilePath(jMsg.file);

        // 下载结束的回到方法
        ClientCallBack callBack = new ClientCallBack() {
            /**
             * 下载成功
             * @param data
             */
            public void onSuccess(Object data) {
                String filePath = (String) data;
                Log.e(ConstantsCommon.LOG_TAG, "下载结束:播放:" + filePath);
                XMediaPlayer.play(filePath);
            }

            /**
             * 下载结束
             * @param message
             */
            public void onFailure(String message) {

            }
        };

        // 下载结束之后，调用回调方法
        new DownLoadFileTask(fileUrl, filePath, callBack).start();
    }


    private static String getDownloadFilePath(String fileUrl)
    {
        String fileName = fileUrl.substring(fileUrl.indexOf("/") + 1);

        String dowloadDir = Environment.getExternalStorageDirectory()
                + "/download/";
        File file = new File(dowloadDir);
        //创建下载目录
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = dowloadDir + fileName;
        return filePath;
    }



}
