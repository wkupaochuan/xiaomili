package sdk;

import android.content.Context;
import android.os.Bundle;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

public class BaiduVoice {

	/**
	 * ��ʼ���ٶ�����ʶ�𴰿�
	 */
	public static BaiduASRDigitalDialog  initBaiduVoiceDialog(Context view, DialogRecognitionListener mRecognitionListener)
	{
	    BaiduASRDigitalDialog mDialog = null;
		Bundle params= new Bundle();
		
		//���ÿ��� API Key 
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 
		
		//���ÿ���ƽ̨ Secret Key 
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 
		
		//������������:������ͨ��,��������,Ӣ��,��ѡ��Ĭ��Ϊ������ͨ��
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
		
		// ��������
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);
		
		// ���ñ����
		params.putBoolean(BaiduASRDigitalDialog.PARAM_PUNCTUATION_ENABLE, false);
		
		// ʵ�������
		mDialog = new BaiduASRDigitalDialog(view, params);
		
		// ���ûص�
		mDialog.setDialogRecognitionListener(mRecognitionListener);
		
		return mDialog;
	}
	
}
