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
	
	
	// �����±�ǩ
	private ImageButton btnStoryTab = null;
	
	// ֪ʶѧϰ��ǩ
	private ImageButton btnKnowledgeTab = null;
	
	// ͨ����ǩ
	private ImageButton btnImTab = null;
	
	// �ػ���ǩ
	private ImageButton btnShutdownTab = null;


	/**
	 * ��ʼ��ҳ��
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        // ��ʼ��tabs
        this.initTabs();
    }
    
    
    
    /**
     * ��ʼ��tabs
     */
    private void initTabs(){
    	// ʵ������ť��
    	this.btnStoryTab = (ImageButton) findViewById(R.id.btn_story_tab);
    	
    	this.btnKnowledgeTab = (ImageButton) findViewById(R.id.btn_knowledge_tab);
    	
    	this.btnImTab = (ImageButton) findViewById(R.id.btn_im_tab);
    	
    	this.btnShutdownTab = (ImageButton) findViewById(R.id.btn_showdown_tab);
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
    
    
    
    
    
}
