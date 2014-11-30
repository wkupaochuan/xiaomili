package com.ai.ui;


import base_ui.BaseActivity;

import com.ai.welcome.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Welcome extends BaseActivity {

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
    
    
    /**
     * 点击听课程按钮
     * @param view
     */
    public void learnTabOnClick (View view){
    	
    	// 跳转到学习课程页面
		Intent intent = new Intent(this, LearnHome.class);
		startActivity(intent);
    }
    
    
}
