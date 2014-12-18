package tools;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {


    public static void main(String[] agrs)
    {
        Bitmap bitmap = getHttpBitmap("http://m4.auto.itc.cn/car/120/95/90/Img1579095_120.jpg");
    }


    /**
     * 获取网落图片资源
     * @param imageUrl
     * @return
     */
    public static Bitmap getHttpBitmap(String imageUrl){

        Bitmap bitmap=null;
        try{
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream =  conn.getInputStream();//通过输入流获取图片数据

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[]  buffer = new byte[1204];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1)
            {
                outStream.write(buffer,0,len);
            }
            inStream.close();
            byte[] data = outStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);//生成位图
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }


}
