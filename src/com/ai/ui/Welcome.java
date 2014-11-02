package com.ai.ui;


import com.ai.welcome.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class Welcome extends ActionBarActivity {
	


	public static String fileDir;

	/**
	 * 初始化页面
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    
    
    /**
     * 听故事按钮点击事件
     * @param view
     */
    public void storyTabOnClick (View view){
    	
    	// 跳转到听故事的主页面
		Intent intent = new Intent(this, StoryHome.class);
		startActivity(intent);
    }
    
    
    public void downloadStorys(View view){
    	fileDir = this.getFilesDir().toString();
    	Log.e("hah", fileDir);
    	new Thread(runnable).start();
    }
    
    
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
        	DownloadService.downlaod();
        }
    };
    
    
    
    
    
}
