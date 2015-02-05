package api;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import constants.ApiURL;
import constants.ConstantsCommon;
import ui.Welcome;


/**
 * 网络请求基类
 * 1--单例模式
 */
public class RestClient {


    private static AsyncHttpClient client = new AsyncHttpClient();


    /**
     * get 方式
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        Log.e(ConstantsCommon.LOG_TAG, url);
        RestClient.client.get(getAbsoluteUrl(url), params, responseHandler);
    }


    public static void getWeb(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        RestClient.client.get(context, url, params, responseHandler);
    }


    /**
     * post方式
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        params.add("toy_unique_id", Welcome.deviceId);
        Log.e(ConstantsCommon.LOG_TAG, "发送请求:" + getAbsoluteUrl(url));
        RestClient.client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl)
    {
        client.setTimeout(10*1000);
        client.setMaxConnections(5);
        return ApiURL.API_HOME + relativeUrl;
    }

}
