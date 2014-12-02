package tools;

import android.content.Context;
import android.os.Bundle;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

public class BaiduVoice {


	public static BaiduASRDigitalDialog  initBaiduVoiceDialog(Context view, DialogRecognitionListener mRecognitionListener)
	{
	    BaiduASRDigitalDialog mDialog = null;
		Bundle params= new Bundle();

		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 

		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 


		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);


		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);


		params.putBoolean(BaiduASRDigitalDialog.PARAM_PUNCTUATION_ENABLE, false);


		mDialog = new BaiduASRDigitalDialog(view, params);


		mDialog.setDialogRecognitionListener(mRecognitionListener);
		
		return mDialog;
	}
	
}
