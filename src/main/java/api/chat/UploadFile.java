package api.chat;


import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import api.ClientCallBack;
import api.RestClient;
import constants.ApiURL;
import constants.ConstantsCommon;
import model.story.StoryItem;

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


    private static String getFileName(String response)
    {
        String fileName = null;
        try {
            JSONArray jsonArrayRes = new JSONArray(response);

            for (int i = 0; i < jsonArrayRes.length(); ++i) {
                StoryItem storyItem = new StoryItem();

                JSONObject obj = jsonArrayRes.getJSONObject(i);
                storyItem.setTitle(obj.getString("name"));
                String path = obj.getString("path");
                String storyCover = obj.getString("story_cover_path");
                storyItem.setLocation(path);
                storyItem.setStoryCover(storyCover);

            }

        } catch (JSONException e) {
        }

        return fileName;
    }

}
