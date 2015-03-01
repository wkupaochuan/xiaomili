package ui;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ai.welcome.R;
import com.alibaba.fastjson.JSON;
import com.gauss.recorder.SpeexRecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import api.ClientCallBack;
import api.chat.DownLoadFileTask;
import api.chat.SendMsg;
import api.chat.UploadFile;
import constants.ConstantsCommon;
import model.chat.JsonMessage;
import service.chat.ChatConnectManager;
import tools.XMediaPlayer;
import tools.media_record.MediaRecordFunc;


/**
 * 亲子互动模块
 */
public class ChatActivity extends BaseActivity{

    private EditText tvMsg;


    //语音文件保存路径
    private String FileName = null;

    private String newVoiceMsgFilePath;

    SpeexRecorder recorderInstance = null;


    private TextView tvNewMsgNotice;
    private TextView tvTextMsg;
    private Button btnPlayVoiceMsg;
    private TextView tvUniqueId;

    public Handler newMsgHandler;


    /**
     * 初始化
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        this.tvMsg = (EditText)this.findViewById(R.id.tv_sms);

        // 使用deviceid登陆(如果用户不存在，注册后登陆)
        ChatConnectManager.login(Welcome.deviceId, this);

        //设置sdcard的路径
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/audiorecordtest.speex";

        this.recorderInstance = new SpeexRecorder(FileName);

        this.tvNewMsgNotice = (TextView)this.findViewById(R.id.tv_new_msg_notice);
        this.tvTextMsg = (TextView)this.findViewById(R.id.tv_text_msg);
        this.tvUniqueId = (TextView)this.findViewById(R.id.tv_unique_id);
        this.btnPlayVoiceMsg = (Button)this.findViewById(R.id.btn_play_voice_msg);

        // 设置unique_id
        this.tvUniqueId.setText("请在微信回复:gz:" + Welcome.deviceId + ", 绑定好友关系");

        // 新消息更新handler
        this.newMsgHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                String msgBody = b.getString("msg_body");
                ChatActivity.this.showMsg(msgBody);
            }
        };
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
        // 开始录音
        MediaRecordFunc.getInstance().startRecord();
    }


    /**
     * 结束录音
     * @param view
     */
    public void stopRecordOnClick(View view){

        String filePath = MediaRecordFunc.getInstance().stopRecord();

        File testFile = new File(filePath);
        if(testFile.exists())
        {
            Log.e(ConstantsCommon.LOG_TAG, "文件存在, 大小:" + testFile.length());
            // 上传文件到服务器
            this.upload(filePath);
        }


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

                Log.e(ConstantsCommon.LOG_TAG, "wechat成功上传文件:" + fileUrl);

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
     * 播放语音消息
     * @param mediaUrl
     */
    private void playVoiceMsg(String mediaUrl)
    {
        Log.e(ConstantsCommon.LOG_TAG, "语音消息:" + mediaUrl);

        // 获取本地存储路径
        this.newVoiceMsgFilePath = ChatActivity.getDownloadFilePath(mediaUrl);

        // 下载结束展示语音播放键
        this.btnPlayVoiceMsg.setVisibility(View.VISIBLE);

        // 下载结束的回到方法
        ClientCallBack callBack = new ClientCallBack() {
            /**
             * 下载成功
             * @param data
             */
            public void onSuccess(Object data) {
                String filePath = (String) data;
                Log.e(ConstantsCommon.LOG_TAG, "下载结束:播放:" + filePath);
            }

            /**
             * 下载失败
             * @param message
             */
            public void onFailure(String message) {
                Log.e(ConstantsCommon.LOG_TAG, "下载失败:" + message);
            }
        };

        // 下载结束之后，调用回调方法
        new DownLoadFileTask(mediaUrl, this.newVoiceMsgFilePath, callBack).start();
    }


    /**
     * 播放新语音消息点击事件
     * @param view
     */
    public void playNewVoiceMsg(View view)
    {
        // 播放新语音消息
        XMediaPlayer.play(this.newVoiceMsgFilePath);

        // 隐藏播放键
        ChatActivity.this.btnPlayVoiceMsg.setVisibility(View.INVISIBLE);
    }


    /**
     * 展示文字消息内容
     * @param msgContent
     */
    private void showTextMsg(String msgContent)
    {
        Log.e(ConstantsCommon.LOG_TAG, "文字消息:" + msgContent);
        // 隐藏语音消息播放键
        this.btnPlayVoiceMsg.setVisibility(View.INVISIBLE);
        // 展示消息
        this.tvTextMsg.setText(msgContent);
    }


    /**
     * 播放消息
     * @param strMsg
     */
    private void showMsg(String strMsg)
    {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        this.tvNewMsgNotice.setText("新消息到达:" + df.format(new Date()).toString());
        Log.e(ConstantsCommon.LOG_TAG, "接收到消息:" + strMsg);
        JsonMessage jMsg = JsonMessage.parse(strMsg);
        if(jMsg.messageType.equals("text"))
        {
            showTextMsg(jMsg.text);
        }
        else if (jMsg.messageType.equals("voice"))
        {
            playVoiceMsg(jMsg.file);
        }
    }


    /**
     * 获取路径
     * @param fileUrl
     * @return
     */
    private static String getDownloadFilePath(String fileUrl)
    {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

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
