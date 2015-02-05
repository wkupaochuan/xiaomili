package ui;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ai.welcome.R;
import com.alibaba.fastjson.JSON;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.recorder.SpeexRecorder;

import java.io.File;

import api.ClientCallBack;
import api.chat.DownLoadFileTask;
import api.chat.SendMsg;
import api.chat.UploadFile;
import constants.ConstantsCommon;
import model.chat.JsonMessage;
import service.chat.ChatConnectManager;


/**
 * 亲子互动模块
 */
public class ChatActivity extends BaseActivity{

    private EditText tvMsg;

    private MediaRecorder mRecorder = null;

    //语音文件保存路径
    private String FileName = null;

    SpeexRecorder recorderInstance = null;


    /**
     * 初始化
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        this.tvMsg = (EditText)this.findViewById(R.id.tv_sms);

        // 使用deviceid登陆(如果用户不存在，注册后登陆)
        ChatConnectManager.login(Welcome.deviceId);

        //设置sdcard的路径
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audiorecordtest.spx";

        this.recorderInstance = new SpeexRecorder(FileName);
    }


    /**
     * 发送文字消息点击事件
     * @param view
     */
    public void sendMessageOnClick(View view)
    {
        String msg = this.tvMsg.getText().toString();
        SendMsg.sendTextMsg(Welcome.deviceId, msg, new ClientCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.e(ConstantsCommon.LOG_TAG, "device_id:" + Welcome.deviceId);
                Log.e(ConstantsCommon.LOG_TAG, "发送文字消息成功:" + data.toString());
            }

            @Override
            public void onFailure(String message) {

            }
        });



    }



    /**
     * 发送多媒体消息
     * 1--暂时给test1用户发消息
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
     * 开始录音点击事件
     * @param view
     */
    public void startRecordOnClick(View view)
    {
        Thread th = new Thread(recorderInstance);
        th.start();
        this.recorderInstance.setRecording(true);
    }


    /**
     * 结束录音
     * @param view
     */
    public void stopRecordOnClick(View view){
        this.recorderInstance.setRecording(false);

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

                Log.e(ConstantsCommon.LOG_TAG, "wechat成功上传文件" + fileUrl);

//                ChatActivity.this.sendMediaMessage(fileUrl);

                sendVoiceMsg(fileUrl);
            }

            // 请求失败
            public void onFailure(String message) {
                Log.e(ConstantsCommon.LOG_TAG, message);
            }

        });

    }


    /**
     * 发送语音消息
     * @param filePath
     */
    private void sendVoiceMsg(String filePath)
    {
        SendMsg.sendVoiceMsg(Welcome.deviceId, filePath, new ClientCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.e(ConstantsCommon.LOG_TAG, "device_id:" + Welcome.deviceId);
                Log.e(ConstantsCommon.LOG_TAG, "发送语音消息成功:" + data.toString());
            }

            @Override
            public void onFailure(String message) {

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
//                XMediaPlayer.play(filePath);
                SpeexPlayer splayer;
                splayer = new SpeexPlayer(filePath);
                splayer.startPlay();
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


    /**
     * 获取路径
     * @param fileUrl
     * @return
     */
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
