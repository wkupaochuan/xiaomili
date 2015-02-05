package api.chat;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import api.ClientCallBack;
import api.RestClient;
import constants.ApiURL;


public class SendMsg {

    public static final String MSG_TYPE_TEXT = "text";
    public static final String MSG_TYPE_VOICE = "voice";


    /**
     * 发送文字类型的消息
     * @param user
     * @param msgContent
     * @param callback
     */
	public static void sendTextMsg(String user, String msgContent, final ClientCallBack callback) {

        // 构造参数
		RequestParams params = new RequestParams();
		params.add("toyUser", user);
        params.add("messageType", SendMsg.MSG_TYPE_TEXT);
        params.add("content", msgContent);

        // 发送请求
		RestClient.post(ApiURL.WECHAT_SEND_MSG, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String res = new String(responseBody);

                    // 返回结果
                    callback.onSuccess(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 请求失败
             */
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                callback.onFailure(new String(responseBody));
            }


        });
	}


    /**
     * 发送语音类型的消息
     * @param user
     * @param filePath
     * @param callback
     */
    public static void sendVoiceMsg(String user, String filePath, final ClientCallBack callback) {

        // 构造参数
        RequestParams params = new RequestParams();
        params.add("toyUser", user);
        params.add("messageType", SendMsg.MSG_TYPE_VOICE);
        params.add("filePath", filePath);

        // 发送请求
        RestClient.post(ApiURL.WECHAT_SEND_MSG, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String res = new String(responseBody);

                    // 返回结果
                    callback.onSuccess(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            /**
             * 请求失败
             */
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                callback.onFailure(new String(responseBody));
            }


        });
    }


	
}
