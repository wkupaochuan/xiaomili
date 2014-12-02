package ui;


import com.ai.welcome.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Welcome extends BaseActivity {

    /**
     * 构建窗口
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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

		Intent intent = new Intent(this, Study.class);
		startActivity(intent);
    }
    
    
}
