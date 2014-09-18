package com.ai.welcome;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class Welcome extends ActionBarActivity {
	
	
	// 听故事标签
	private ImageButton btnStoryTab = null;
	
	// 知识学习标签
	private ImageButton btnKnowledgeTab = null;
	
	// 通话标签
	private ImageButton btnImTab = null;
	
	// 关机标签
	private ImageButton btnShutdownTab = null;


	/**
	 * 初始化页面
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        // 初始化tabs
        this.initTabs();
    }
    
    
    
    /**
     * 初始化tabs
     */
    private void initTabs(){
    	// 实例化按钮们
    	this.btnStoryTab = (ImageButton) findViewById(R.id.btn_story_tab);
    	
    	this.btnKnowledgeTab = (ImageButton) findViewById(R.id.btn_knowledge_tab);
    	
    	this.btnImTab = (ImageButton) findViewById(R.id.btn_im_tab);
    	
    	this.btnShutdownTab = (ImageButton) findViewById(R.id.btn_showdown_tab);
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
    
    
    
    
    
}
