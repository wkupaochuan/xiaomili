package com.ai.ui;

import java.util.ArrayList;
import java.util.List;

import model.LearnItemModel;
import model.LetterLearnResult;

import service.CheckLearnResult;
import service.LearnListService;
import tools.XMediaPlayer;

import base_ui.BaseActivity;
import com.ai.welcome.R;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import constants.ConstantsCommon;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LearnHome extends BaseActivity{
	
	private BaiduASRDigitalDialog mDialog = null;
	private DialogRecognitionListener mRecognitionListener;
	
	private List<LearnItemModel> learnList;
	
	// 正在播放的(-1表示无)
	private int playingId = -1;
	
	LinearLayout ll;
	
	/**
	 * 初始化窗口
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_home);
        
        // 设置百度语音回调
        this.initBiduVoiceRecgnitionListener();
        
        // 获取唐诗音频
        this.learnList = LearnListService.getList();
        
        //动态添加
         this.ll =  (LinearLayout) this.findViewById(R.id.text_line);
         this.ll.setOrientation(LinearLayout.VERTICAL);
        
        // 播放
        this.playNext();
        
        // 设置识
        setOnCompletionListener();
    }
    
    
    /**
     * 调用百度语音识别
     * @param view
     */
	public void invokeBaiduVoice(View view){
		this.initBaiduVoiceDialog();
	}
	
	

	/**
	 * 初始化百度语音识别窗口
	 */
	public void initBaiduVoiceDialog()
	{
		// 释放
        if (this.mDialog != null) {
        	this.mDialog.dismiss();
        }
		
		Bundle params= new Bundle();
		
		//设置开放 API Key 
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 
		
		//设置开放平台 Secret Key 
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 
		
		//设置语种类型:中文普通话,中文粤语,英文,可选。默认为中文普通话
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
		
		// 设置主题
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);
		
		// 实例化输入框
		mDialog = new BaiduASRDigitalDialog(this,params);
		
		// 设置回调
		mDialog.setDialogRecognitionListener(this.mRecognitionListener);

		// 展示
		mDialog.show();		
	}
	
	
    /**
     * 设置百度语音识别回调
     */
    private void initBiduVoiceRecgnitionListener(){
    	
		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){
				
				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;
				
				if(rs != null && rs.size() > 0){
					
					for(String str:rs){
						Log.e(ConstantsCommon.LOG_TAG, "百度语音识别结果:" + str.toString());
					}
					afterRecgnition(rs.get(0));
				}
				
			}
            
		};
    }
    
    
    /**
     * 识别结束回调
     * @param result
     */
    public void afterRecgnition(String result)
    {
    	// 去除识别结果的标点符号
//    	result = result.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
    	String classText = this.learnList.get(this.playingId).getMediaText();
    	Log.e(ConstantsCommon.LOG_TAG, "原文:" + classText + "; 识别结果:" + result);
    	
    	// 给结果打分
    	LetterLearnResult learnResult 
    	= CheckLearnResult.gradeRepeatedCentence(classText, result);
    	
    	// 根据结果更新字符串
    	String learnResultText = learnResult.getResString();
    	this.updateSentence(learnResultText, this.playingId);
    	
    	// 分数太低，重新播放
    	if(learnResult.getGrade() < 0.5)
    	{
    		
    	}
    	else{
    		
    	}
    	
    	// 播放下一首
    	this.playNext();
    }
    
    
    /**
     * 播放下一句
     */
    public void playNext()
    {
    	// 没播放
    	if(this.playingId == -1)
    	{
    		this.playingId = 0;
    		this.addCentence(this.learnList.get(this.playingId).getMediaText(), this.playingId);
    		XMediaPlayer.play(this.learnList.get(this.playingId).getMediaUrl());
    	}
    	// 最后
    	else if(this.playingId >= this.learnList.size() - 1)
    	{
    		this.playingId = -1;
    	}
    	// 中间
    	else{
    		this.playingId++;
    		this.addCentence(this.learnList.get(this.playingId).getMediaText(), this.playingId);
    		XMediaPlayer.play(this.learnList.get(this.playingId).getMediaUrl());
    		
    	}
    	Log.e(ConstantsCommon.LOG_TAG, "playingid is " + this.playingId);
    }
    
    
    /**
     * 新增一句话
     * @param text
     */
    private void addCentence(String text, int ViewId)
    {
    	TextView tx = new TextView(LearnHome.this);
    	tx.setId(ViewId);
    	tx.setTextSize(30);
    	tx.setText(text);
    	this.ll.addView(tx);
    }
    
    
    /**
     * 更新页面展示的字符串
     * @param text
     * @param viewId
     */
    private void updateSentence(String text, int viewId)
    {
        TextView tv = (TextView) this.findViewById(viewId);
    	
    	tv.setText(Html.fromHtml(text));
    }
    
    
	/**
	 * 设置单个音频播放完毕事件
	 */
	private void setOnCompletionListener(){
		XMediaPlayer.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        
	        public void onCompletion(MediaPlayer arg0) {
	        	initBaiduVoiceDialog();
	        }
		});
	}

    
}