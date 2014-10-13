package config;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class QYRestClient {

	  private static AsyncHttpClient client = new AsyncHttpClient();
	  
	  public static AsyncHttpClient getIntance() {
		  return client;
	  }

	  /**
	   * 发送get请求
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
	   * 发送post请求
	   * @param url
	   * @param params
	   * @param responseHandler
	   */
	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  Log.e("hah", getAbsoluteUrl(url));
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(context, getAbsoluteUrl(url), params, responseHandler);
	  }

	  /**
	   * 获取完整路径
	   * @param relativeUrl
	   * @return
	   */
	  private static String getAbsoluteUrl(String relativeUrl) {
		  client.setTimeout(10*1000);
		  client.setMaxConnections(5);
	      return "http://toy.wkupaochuan.com/" + relativeUrl;
	  }
}
