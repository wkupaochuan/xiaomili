package base_ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends ActionBarActivity {
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //����ʾ����ı�����
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        //����ʾϵͳ�ı�����          
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN );
	}

}