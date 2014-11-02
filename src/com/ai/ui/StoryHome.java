package com.ai.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ai.constants.ConstantsCommon;
import com.ai.sdk.ApiClent;
import com.ai.sdk.ApiClent.ClientCallback;
import com.ai.welcome.R;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class StoryHome extends ActionBarActivity{
	
	// �����б?��
	private ListView lvStoryList;
	
	// �����б�
	private List<StoryItem> storyList = new ArrayList<StoryItem>();
	
	// ȫ�����¼���(ÿ�ζ����������ѯ��Ȼ��ֵ��storyList)
	private List<StoryItem> storyBook = new ArrayList<StoryItem>();
	
	// �ٶ�����ʶ��Ի���
    private BaiduASRDigitalDialog mDialog = null;

    // �ٶ�����ʶ���������
    private DialogRecognitionListener mRecognitionListener;
    
    // ���š���ͣ��ť
    private ImageButton playOrPauseButton;
    
	
	/**
	 * ��ʼ��ҳ��
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_home);
        
        // ���ù����б?��
        //this.initStoryListView();
        

        // ��ʼ���ٶ�����ʶ���������
        this.initBiduVoiceRecgnitionListener();
        
        // ��ʼ�����Ű�ť
        this.playOrPauseButton = (ImageButton) this.findViewById(R.id.btn_play_or_pause);
        
        // �ӷ�������ȡ������Դ
        this.getStoryListFromServer();
    }
    
    
    /**
     * ��ʼ���ٶ�����ʶ���������
     */
    private void initBiduVoiceRecgnitionListener(){
    	
		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){
			//�� Results �л�ȡ Key Ϊ DialogRecognitionListener .RESULTS_RECOGNITION �� StringArrayList
			// ,����Ϊ�ա���ȡ��ʶ�����ִ����Ӧ��ҵ���߼�����,�˻ص��������̵߳��á� 
				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;
				
				if(rs != null && rs.size() > 0){
					//mDialog.dismiss();
					//�˴�����ʶ����,ʶ��������ж��,�����ŶȴӸߵ�������,��һ��Ԫ�������Ŷ���ߵĽ��
					for(String str:rs){
						Log.e("hah", "��˵����:" + str.toString());
					}
					
					// ʶ����ϣ������
					if(MediaPlayerHandler.getInstance().isPaused()){
						playOrPauseOnclick(null);
					}
					
					// ���ʶ������¹����б�
					updateStoryListByInputStoryTitle(rs.get(0));
					
				}
				
			}
            
		};
    }
    
    
    /**
     * ��ʼ�������б�
     */
    private void initStoryListView(List<StoryItem> storyList){
    	// ��ʼ�������б?��
    	this.lvStoryList = (ListView) this.findViewById(R.id.lv_music_list);
    	
    	// ��ȡ�����б�
    	this.storyList = storyList;
    	
    	// ����ȫ�������б�
    	this.storyBook = this.storyList;
    	
    	// ���ù����б�ҳ������
    	this.updateStoryList(this.storyList);
    	
    }
    
    
    /**
     * ���Ź���
     * @param storyId
     */
    private void playStory(int storyId){
    	
    	MediaPlayerHandler.getInstance().playStoryById(storyId);
    	
    }
    
    
    /**
     * ���¹����б�
     * @param newStoryList
     */
    private void updateStoryList(List<StoryItem> newStoryList){
    	// ���²���ҳ��Ĺ����б�
    	this.storyList = newStoryList;
    	
    	// ͬ�����²������Ĺ����б�
    	MediaPlayerHandler.getInstance().updateStoryList(this.storyList);
    	
    	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
    	
    	for(StoryItem storyItem:this.storyList){
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("story_item_title", storyItem.getTitle());
    		listMap.add(map);
    	}
    
    	
    	// ���ù����б�ҳ������
    	this.lvStoryList.setAdapter(
    			new SimpleAdapter(
    					StoryHome.this
    					, listMap
    					, R.layout.story_item
    					, new String[]{"story_item_title"} 
    					, new int[]{ R.id.story_item_title}
    					)
    			);
    	
    	// ���ù�����Ŀ����¼�
    	this.setStoryOnclickEvent();
    	
    }
    
    
    
    /**
     * ������һ������ť����¼�
     * @param view
     */
    public void playNextOnclick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playNextStory();
    }

    
    /**
     * ������һ������ť����¼�
     * @param view
     */
    public void playForwardStoryOnClick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playForwardStory();
    }
    
    
    /**
     * ������ͣ�����ż�
     * @param view
     */
    public void playOrPauseOnclick(View view){
    	
    	if(MediaPlayerHandler.getInstance().isPaused()){
    		MediaPlayerHandler.getInstance().continutePlaying();
    		this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	}
    	else if(MediaPlayerHandler.getInstance().isPlaying()){
    		MediaPlayerHandler.getInstance().pause();
    		this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PLAYING);
    	}
    	else if(MediaPlayerHandler.getInstance().isStoped()){
    		MediaPlayerHandler.getInstance().play();
    		this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	}
    	
    }
    
    /**
     * ���²���״̬
     */
    private void updatePlayStatus(int playingStatus)
    {
    	if(playingStatus == MediaPlayerHandler.PLAYING_STATUS_PAUSE){
    		this.playOrPauseButton.setBackgroundResource(R.drawable.pause_selecor);
    	}
    	else if(playingStatus == MediaPlayerHandler.PLAYING_STATUS_PLAYING){
    		this.playOrPauseButton.setBackgroundResource(R.drawable.play_selecor);
    	}
    }
    

    
    
    /**
     * ���ù��µ���¼�
     */
    private void setStoryOnclickEvent(){
    	
    	this.lvStoryList.setOnItemClickListener(new OnItemClickListener(){
    		
    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
    			playStory((int)id);
			}
    		
    	});
    }
    
    /**
     * ��ȡ�����б�
     * @return
     */
    private List<StoryItem> getStoryListFromDisk(){
    	List<StoryItem> storyList = new ArrayList<StoryItem>();
    	Cursor storyCursors = this.searchWithPath();
    	Log.e("hah", "�ܹ��й���:" + storyCursors.getCount());
    	storyCursors.moveToFirst();
    	
    	for(int i  = 0; i < storyCursors.getCount(); ++i){
    		StoryItem storyItem = new StoryItem();
    		// ���ù��±���
    		String storyTitle = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.TITLE));
    		// ���ù��´洢��ַ
    		String storyLocation = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.DATA));
    		
    		storyItem.setTitle(storyTitle);
    		storyItem.setLocation(storyLocation);
    		
    		// �ƶ�����һ��
    		storyCursors.moveToNext();
    		
    		// ��ӵ������б�
    		storyList.add(storyItem);
    	}
    	
    	return storyList;
    }
    
    
    
    /**
     * ��ȡ�ֻ�musicĿ¼�µ������ļ�
     * @param path
     */
	private Cursor searchWithPath() {
		//String path = "/storage/emulated/0/Music";
		//String path = this.getFilesDir().toString() + "/";
		String path = "/";
		//String path = "/data/data/com.ai.welcome/";
	    String[] projection = new String[] { MediaStore.Audio.Media._ID,
	    MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA ,  MediaStore.Audio.Media.DURATION,
	    MediaStore.Audio.Media.ARTIST};
	  
	    Cursor cursor = this.getContentResolver().query(
	    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
	    MediaStore.Audio.Media.DATA + " like '" + path + "%'", null, null);
	    cursor.moveToFirst();
	  
//		  String TAG = "hah";
//		  for (int j = 0; j < 1; j++) {
//		   Log.e(TAG,cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
//		   Log.e(TAG,cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
//		   Log.e(TAG,cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
//		   Log.e(TAG,cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
//		   Log.e(TAG,cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
//		   cursor.moveToNext();
//		  }
	    
	    return cursor;
	}
	
	
	
	/**
	 * �������Ĺ�����Ƹ��¹����б�
	 * @param needleStoryTitle
	 */
	private void updateStoryListByInputStoryTitle(String needleStoryTitle){
		
		List<StoryItem> hintStoryList = StoryListHandler.getMostSimilarStorys(this.storyBook, needleStoryTitle);
		
		
		this.updateStoryList(hintStoryList);
	}
	
	
	
	/**
	 * ���ðٶ������Ի���
	 * @param view
	 */
	public void invokeBaiduVoice(View view){
		
		// ������ڲ��Ź���, ����ֹͣ��ǰ�Ĳ��ţ�����Ӱ������ʶ��Ľ��
		if(MediaPlayerHandler.getInstance().isPlaying()){
			this.playOrPauseOnclick(null);
		}
		
        if (mDialog != null) {
            mDialog.dismiss();
        }
		
		Bundle params= new Bundle();
		
		//���ÿ��� API Key 
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 
		
		//���ÿ���ƽ̨ Secret Key 
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 
		
		//����ʶ������:���������롢��ͼ������......,��ѡ��Ĭ��Ϊ���롣
//		params.putInt( BaiduASRDigitalDialog.PARAM_PROP, VoiceRecognitionConfig.PROP_INPUT);
		
		//������������:������ͨ��,��������,Ӣ��,��ѡ��Ĭ��Ϊ������ͨ��
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
		
		//�����Ҫ�������,�����·���������Ϊ���벻֧�� 
//		params.putBoolean(BaiduASRDigitalDialog.PARAM_NLU_ENABLE, true);
		
		// ���öԻ�������,��ѡ��BaiduASRDigitalDialog �ṩ�����������졢�̡���������ɫ,ÿ���� ɫ�ַ�����������ɫ����
		// �� 8 ������,�����߿��԰���ѡ��,ȡֵ�ο� BaiduASRDigitalDialog �� ǰ׺Ϊ THEME_�ĳ�����Ĭ��Ϊ����ɫ 
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);
		
		mDialog = new BaiduASRDigitalDialog(this,params);
		mDialog.setDialogRecognitionListener(this.mRecognitionListener);

		mDialog.show();		
		
	}
	
	
	/**
	 * �ӷ�������ȡ������Դ
	 */
	private void getStoryListFromServer()
	{
		// ���͵�½����
		ApiClent.getStoryList("", new ClientCallback() {
						
			// �ɹ�
			public void  onSuccess(Object data) {
				
				String stringRes = data.toString();
				List<StoryItem> storyList = new ArrayList<StoryItem>();
				try {
					JSONArray jsonArrayRes = new JSONArray(stringRes);
			    	
					for(int i =0; i < jsonArrayRes.length(); ++i){
						StoryItem storyItem = new StoryItem();
						
						JSONObject obj = jsonArrayRes.getJSONObject(i);
			    		storyItem.setTitle(obj.getString("name"));
			    		String path = obj.getString("path");
			    		storyItem.setLocation(path);
			    		
			    		storyList.add(storyItem);
					}
					
					StoryHome.this.initStoryListView(storyList);
				} catch (JSONException e) {
				}

				
			}
			
			// ʧ��
			public void onFailure(String message) {
				Log.e(ConstantsCommon.LOG_TAG, message);
			}
			
			// ����
			public void onError(Exception e) {
				Log.e(ConstantsCommon.LOG_TAG, e.getMessage());
			}
		});
	}
		

}
