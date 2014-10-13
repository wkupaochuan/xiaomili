package config;


import org.apache.http.Header;


import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


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
	

	public static void getStoryList(String searchWords, final ClientCallback callback) {
		
		RequestParams params = new RequestParams();
		params.add("searchWords", searchWords);

		QYRestClient.post("mp3/mp3/get_story_list_for_mobile", params, new AsyncHttpResponseHandler() {

			/**
			 * 请求成功
			 */
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					Log.e("hah", new String(responseBody));
					Log.e("hah", headers.toString());
					Log.e("hah", statusCode + "");
//					UserEntity user = UserEntity.parse(new String(responseBody));
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
				
				Log.e("hah", new String(responseBody));
				Log.e("hah", headers.toString());
				Log.e("hah", statusCode + "");
//				callback.onFailure(message_error);
			}
		});
	}
	
}
