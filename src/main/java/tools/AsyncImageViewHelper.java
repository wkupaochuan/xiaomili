package tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import constants.ConstantsCommon;

/**
 * 异步加载图片工具类
 */

public class AsyncImageViewHelper extends AsyncTask<String, Integer, Bitmap> {

    private ImageView imageView=null;

    /**
     * 构造方法
     * @param img
     */
    public AsyncImageViewHelper(ImageView img)
    {
        imageView=img;
    }


    /**
     * 下载图片
     * 运行在子线程中
     * @param params
     * @return
     */
    protected Bitmap doInBackground(String... params) {
        try
        {
            String imagePath = params[0];
            Log.e(ConstantsCommon.LOG_TAG, "开始下载图片:" + imagePath);
            URL url=new URL(imagePath);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6* 1000);
            if(conn.getResponseCode()==200)
            {
                InputStream input=conn.getInputStream();
                Bitmap map= BitmapFactory.decodeStream(input);
                Log.e(ConstantsCommon.LOG_TAG, "下载图片结果:" + map.toString());
                return map;
            }
            else{
                Log.e(ConstantsCommon.LOG_TAG, "下载图片时连接错误:" + conn.getResponseCode() + ";" + conn.getResponseMessage());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 设置图片
     * @param result
     */
    protected void onPostExecute(Bitmap result)
    {
        if(imageView!=null && result!=null)
        {
            // 设置图片自适应
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(result);
        }

        super.onPostExecute(result);
    }
}