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
	 * ��ʼ��ҳ��
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    
    
    /**
     * �����°�ť����¼�
     * @param view
     */
    public void storyTabOnClick (View view){
    	
    	// ��ת�������µ���ҳ��
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
