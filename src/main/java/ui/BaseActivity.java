package ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends ActionBarActivity {

    private static BaseActivity instance;

	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全屏设置
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // 禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                String DeviceId = Settings.Secure.getString(
                this.getContentResolver(), Settings.Secure.ANDROID_ID);


        instance = this;
	}


    public static String getId()
    {
        String DeviceId = Settings.Secure.getString(
                        instance.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return DeviceId;
    }
}
