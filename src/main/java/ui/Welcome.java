package ui;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import com.ai.welcome.R;

import constants.ConstantsCommon;


public class Welcome extends BaseActivity {

    public static String deviceId;

    /**
     * 构建窗口
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
        setContentView(R.layout.welcome_test);


        String DeviceId = Settings.Secure.getString(
                this.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = DeviceId;

        this.test();
    }


    private void test()
    {
        Log.e(ConstantsCommon.LOG_TAG, System.getProperty("java.version").toString());

//        Log.e("xxx唯一id", DeviceId);
//
//        String Imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
//                .getDeviceId();
//
//        Log.e("imei", DeviceId);


    }


    /**
     * 故事页面
     * @param view
     */
    public void storyTabOnClick (View view){


		Intent intent = new Intent(this, Story.class);
		startActivity(intent);
    }


    /**
     * 学习页面
     * @param view
     */
    public void studyTabOnClick (View view){
        Intent intent = new Intent(this, StudyHomeTest.class);
		startActivity(intent);
    }


    /**
     * 聊天页面
     * @param view
     */
    public void chatTabOnClick (View view){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    
    
}
