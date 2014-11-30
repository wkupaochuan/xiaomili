package com.ai.ui;


import base_ui.BaseActivity;

import com.ai.welcome.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Welcome extends BaseActivity {

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
    
    
    /**
     * ������γ̰�ť
     * @param view
     */
    public void learnTabOnClick (View view){
    	
    	// ��ת��ѧϰ�γ�ҳ��
		Intent intent = new Intent(this, LearnHome.class);
		startActivity(intent);
    }
    
    
}
