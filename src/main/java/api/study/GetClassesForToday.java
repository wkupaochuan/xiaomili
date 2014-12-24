package api.study;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import api.ClientCallBack;
import api.RestClient;
import model.study.ClassModel;

public class GetClassesForToday {

    private static  String url = "http://toy-api.wkupaochuan.com/study/user_class/get_classes_for_today";

    /**
     * 获取故事列表
     * @param callback
     */
    public static void getClass(final ClientCallBack callback) {

        RequestParams params = new RequestParams();

        RestClient.post(GetClassesForToday.url, params, new AsyncHttpResponseHandler() {

            /**
             * 请求成功
             */
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ArrayList<ClassModel> course = GetClassesForToday.getClassesFromResponse(new String(responseBody));
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
     * 从服务器返回结果中解析课程数组
     * @param response
     * @return
     */
    private static ArrayList<ClassModel> getClassesFromResponse(String response)
    {
        ArrayList<ClassModel> arrayClasses = new ArrayList<ClassModel>();
        try{
            JSONArray jsonArrayRes = new JSONArray(response);


            for(int i =0; i < jsonArrayRes.length(); ++i){

                JSONObject obj = jsonArrayRes.getJSONObject(i);

                ClassModel tmpClass = new ClassModel();
                tmpClass.setClassTitle(obj.getString("class_title"));
                tmpClass.setClassCover(obj.getString("class_cover"));
                tmpClass.setRateOfProgress(obj.getInt("class_progress"));
                tmpClass.setGradeForClass(obj.getInt("class_grade"));
                arrayClasses.add(tmpClass);
            }
        }
        catch(Exception e)
        {
        }

        return arrayClasses;
    }

}
