package com.ai.sdk;

import android.content.Context;
import android.os.Bundle;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

public class BaiduVoice {

	/**
	 * 初始化百度语音识别窗口
	 */
	public static BaiduASRDigitalDialog  initBaiduVoiceDialog(Context view, DialogRecognitionListener mRecognitionListener)
	{
	    BaiduASRDigitalDialog mDialog = null;
		Bundle params= new Bundle();
		
		//设置开放 API Key 
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 
		
		//设置开放平台 Secret Key 
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 
		
		//设置语种类型:中文普通话,中文粤语,英文,可选。默认为中文普通话
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
		
		// 设置主题
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);
		
		// 禁用标点符号
		params.putBoolean(BaiduASRDigitalDialog.PARAM_PUNCTUATION_ENABLE, false);
		
		// 实例化输入框
		mDialog = new BaiduASRDigitalDialog(view, params);
		
		// 设置回调
		mDialog.setDialogRecognitionListener(mRecognitionListener);
		
		return mDialog;
	}
	
}
