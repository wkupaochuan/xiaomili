package tools;

import android.content.Context;
import android.os.Bundle;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;

public class BaiduVoice {

    /**
     * 获取一个百度语音识别窗口
     * @param view
     * @return
     */
	public static BaiduASRDigitalDialog  initBaiduVoiceDialog(Context view)
	{
	    BaiduASRDigitalDialog mDialog;

		Bundle params= new Bundle();

        // 开发者Key
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is");
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 

        // 设置语言为中文
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);

        // 窗口主题
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);

        // 设置识别结果没有标点
		params.putBoolean(BaiduASRDigitalDialog.PARAM_PUNCTUATION_ENABLE, false);

        // 新建窗口
		mDialog = new BaiduASRDigitalDialog(view, params);

        // 返回
		return mDialog;
	}
	
}
