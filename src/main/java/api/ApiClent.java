package api;


import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import constants.ApiURL;


public class ApiClent {

	/**
	 * 获取故事列表
	 * @param searchWords
	 * @param callback
	 */
	public static void getStoryList(String searchWords, final ClientCallBack callback) {
		
		RequestParams params = new RequestParams();
		params.add("searchWords", searchWords);

		RestClient.post(ApiURL.GET_STORY_LIST, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
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
                callback.onFailure(responseBody.toString());
            }
        });
	}
	
}
