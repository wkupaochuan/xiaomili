package api.story;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.ClientCallBack;
import api.RestClient;
import constants.ApiURL;
import model.StoryItem;


public class GetStoryList {

    /**
     * 获取故事列表
     * @param searchWords           搜索词
     * @param callback              回调方法
     */
	public static void getStoryList(String searchWords, final ClientCallBack callback) {

        // 构造参数
		RequestParams params = new RequestParams();
		params.add("search_words", searchWords);

        // 发送请求
		RestClient.post(ApiURL.GET_STORY_LIST, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // 将返回的json结果组装为故事列表
                    String res = new String(responseBody);
                    ArrayList<StoryItem> storyList = GetStoryList.parseStringToStoryList(res);

                    // 返回结果
                    callback.onSuccess(storyList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 请求失败
             */
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                callback.onFailure(responseBody.toString());
            }
        });
	}


    /**
     * 把返回结果组装成故事列表
     * @param response
     */
    public static ArrayList<StoryItem> parseStringToStoryList(String response)
    {
        ArrayList<StoryItem> storyList = new ArrayList<StoryItem>();
        try {
            JSONArray jsonArrayRes = new JSONArray(response);

            for (int i = 0; i < jsonArrayRes.length(); ++i) {
                StoryItem storyItem = new StoryItem();

                JSONObject obj = jsonArrayRes.getJSONObject(i);
                storyItem.setTitle(obj.getString("name"));
                String path = obj.getString("path");
                storyItem.setLocation(path);

                storyList.add(storyItem);
            }

        } catch (JSONException e) {
        }

        return storyList;
    }
	
}
