package sdk;


import org.apache.http.Header;



import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import constants.ApiURL;


public class ApiClent {
	public final static String message_error = "";
	
	public interface ClientCallback{
        abstract void onSuccess(Object data);
        abstract void onFailure(String message);
        abstract void onError(Exception e);
    }
	
//	private static void saveCache(WCApplication appContext, String key, Serializable entity) {
//    	appContext.saveObject(entity, key);
//    }
	

	/**
	 * 获取故事列表
	 * @param searchWords
	 * @param callback
	 */
	public static void getStoryList(String searchWords, final ClientCallback callback) {
		
		RequestParams params = new RequestParams();
		params.add("searchWords", searchWords);

		QYRestClient.post(ApiURL.GET_STORY_LIST, params, new AsyncHttpResponseHandler() {

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
