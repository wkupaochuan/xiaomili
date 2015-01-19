package model.chat;

import com.alibaba.fastjson.JSON;

/**
 * 消息
 */


public class JsonMessage {
    // 消息类型
    public static String MSG_TYPE_VOICE = "voice";
    public static String MSG_TYPE_TEXT = "text";
    public static String MSG_TYPE_PIC = "pic";

    // 文件地址
    public String file;
    // 消息类型 text(文字), voice(语音), pic(图片)
    public String messageType;
    // 消息内容
    public String text;


    /**
     * 获取object类型的消息
     * @param res
     * @return
     */
    public static JsonMessage parse(String res)  {
        JsonMessage data = new JsonMessage();
        data = JSON.parseObject(res, JsonMessage.class);
        return data;
    }


}
