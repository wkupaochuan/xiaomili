package sdk;

import android.content.Context;
import android.util.Log;

import constants.ApiURL;
import constants.ConstantsCommon;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class QYRestClient {

	  private static AsyncHttpClient client = new AsyncHttpClient();
	  
	  public static AsyncHttpClient getIntance() {
		  return client;
	  }

	  /**
	   * ??????get璇凤拷??
	   * @param url
	   * @param params
	   * @param responseHandler
	   */
	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void getWeb(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(context, url, params, responseHandler);
	  }


	  /**
	   * ??????post璇凤拷??
	   * @param url
	   * @param params
	   * @param responseHandler
	   */
	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  Log.e(ConstantsCommon.LOG_TAG, getAbsoluteUrl(url));
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(context, getAbsoluteUrl(url), params, responseHandler);
	  }

	  /**
	   * ??锟斤拷??锟�???锟借矾锟�?
	   * @param relativeUrl
	   * @return
	   */
	  private static String getAbsoluteUrl(String relativeUrl) {
		  client.setTimeout(10*1000);
		  client.setMaxConnections(5);
	      return ApiURL.API_HOME + relativeUrl;
	  }
}
