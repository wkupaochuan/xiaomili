package api.chat;


import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

import api.ClientCallBack;
import api.RestClient;
import constants.ApiURL;
import constants.ConstantsCommon;

/**
 * 上传文件
 */

public class UploadFile {

    /**
     * 获取故事列表
     * @param filePath           搜索词
     * @param callback              回调方法
     */
    public static void uploadFile(String filePath, final ClientCallBack callback) {

        Log.e(ConstantsCommon.LOG_TAG, "wechat开始上传文件:" + filePath);
        // 构造参数
        RequestParams params = new RequestParams();
        try {
            params.put("Filedata", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 发送请求
        RestClient.post(ApiURL.UPLOAD_FILE_TO_SERVER, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // 返回结果
                    callback.onSuccess(new String(responseBody));
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
