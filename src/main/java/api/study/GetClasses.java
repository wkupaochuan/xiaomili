package api.study;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import api.ClientCallBack;
import api.RestClient;
import model.study.ClassSentenceModel;

public class GetClasses {
    private static  String url = "";

    /**
     * 获取故事列表
     * @param searchWords
     * @param callback
     */
    public static void getClass(String searchWords, final ClientCallBack callback) {

        RequestParams params = new RequestParams();
        params.add("searchWords", searchWords);

        RestClient.post(GetClasses.url, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    List<ClassSentenceModel> course = GetClasses.getClassFromResponse(new String(responseBody));
                    callback.onSuccess(course);
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
     * 从服务器返回结果中获取课程句子数组
     * @param response
     * @return
     */
    private static List<ClassSentenceModel> getClassFromResponse(String response)
    {
        List<ClassSentenceModel> course = new ArrayList<ClassSentenceModel>();
        try{
            JSONArray jsonArrayRes = new JSONArray(response);

            for(int i =0; i < jsonArrayRes.length(); ++i){
                ClassSentenceModel sentence = new ClassSentenceModel();
                JSONObject obj = jsonArrayRes.getJSONObject(i);
                sentence.setMediaText(obj.getString("text"));
                sentence.setMediaUrl(obj.getString("url"));
                course.add(sentence);
            }
        }
        catch(Exception e)
        {
        }

        return course;
    }

}
