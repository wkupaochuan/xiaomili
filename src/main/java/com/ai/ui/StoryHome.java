package com.ai.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.StoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.story.MediaPlayerHandler;
import service.story.StoryListHandler;

import base_ui.BaseActivity;
import constants.ConstantsCommon;
import com.ai.sdk.ApiClent;
import com.ai.sdk.ApiClent.ClientCallback;
import com.ai.welcome.R;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class StoryHome extends BaseActivity{
	
	// 锟斤拷锟斤拷锟叫�?锟斤拷
	private ListView lvStoryList;
	
	// 锟斤拷锟斤拷锟叫憋拷
	private List<StoryItem> storyList = new ArrayList<StoryItem>();
	
	// 全锟斤拷锟斤拷锟铰硷拷锟斤拷(每锟轿讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟窖拷锟饺伙拷锟街碉拷锟絪toryList)
	private List<StoryItem> storyBook = new ArrayList<StoryItem>();
	
	// 锟劫讹拷锟斤拷锟斤拷识锟斤拷曰锟斤拷锟�
    private BaiduASRDigitalDialog mDialog = null;

    // 锟劫讹拷锟斤拷锟斤拷识锟斤拷锟斤拷锟斤拷锟斤拷锟�
    private DialogRecognitionListener mRecognitionListener;
    
    // 锟斤拷锟脚★拷锟斤拷停锟斤拷钮
    private ImageButton playOrPauseButton;
    
	
	/**
	 * 锟斤拷始锟斤拷页锟斤拷
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_home);
        
        // 锟斤拷锟矫癸拷锟斤拷锟叫�?锟斤拷
        //this.initStoryListView();
        

        // 锟斤拷始锟斤拷锟劫讹拷锟斤拷锟斤拷识锟斤拷锟斤拷锟斤拷锟斤拷锟�
        this.initBiduVoiceRecgnitionListener();
        
        // 锟斤拷始锟斤拷锟斤拷锟脚帮拷钮
        this.playOrPauseButton = (ImageButton) this.findViewById(R.id.btn_play_or_pause);
        
        // 锟接凤拷锟斤拷锟斤拷锟斤拷取锟斤拷锟斤拷锟斤拷源
        this.getStoryListFromServer();
    }
    
    
    /**
     * 锟斤拷始锟斤拷锟劫讹拷锟斤拷锟斤拷识锟斤拷锟斤拷锟斤拷锟斤拷锟�
     */
    private void initBiduVoiceRecgnitionListener(){
    	
		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){
			//锟斤拷 Results 锟叫伙拷取 Key 为 DialogRecognitionListener .RESULTS_RECOGNITION 锟斤拷 StringArrayList
			// ,锟斤拷锟斤拷为锟秸★拷锟斤拷取锟斤拷识锟斤拷锟斤拷锟街达拷锟斤拷锟接︼拷锟揭碉拷锟斤拷呒锟斤拷锟斤拷锟�,锟剿回碉拷锟斤拷锟斤拷锟斤拷锟竭程碉拷锟矫★拷 
				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;
				
				if(rs != null && rs.size() > 0){
					//mDialog.dismiss();
					//锟剿达拷锟斤拷锟斤拷识锟斤拷锟斤拷,识锟斤拷锟斤拷锟斤拷锟斤拷卸锟斤拷,锟斤拷锟斤拷锟脚度从高碉拷锟斤拷锟斤拷锟斤拷,锟斤拷一锟斤拷元锟斤拷锟斤拷锟斤拷锟脚讹拷锟斤拷叩慕锟斤拷
					for(String str:rs){
						Log.e("hah", "锟斤拷说锟斤拷锟斤拷:" + str.toString());
					}
					
					// 识锟斤拷锟斤拷希锟斤拷锟斤拷锟斤拷
					if(MediaPlayerHandler.getInstance().isPaused()){
						playOrPauseOnclick(null);
					}
					
					// 锟斤拷锟绞讹拷锟斤拷锟斤拷锟铰癸拷锟斤拷锟叫憋拷
					updateStoryListByInputStoryTitle(rs.get(0));
					
				}
				
			}
            
		};
    }
    
    
    /**
     * 锟斤拷始锟斤拷锟斤拷锟斤拷锟叫憋拷
     */
    private void initStoryListView(List<StoryItem> storyList){
    	// 锟斤拷始锟斤拷锟斤拷锟斤拷锟叫�?锟斤拷
    	this.lvStoryList = (ListView) this.findViewById(R.id.lv_music_list);
    	
    	// 锟斤拷取锟斤拷锟斤拷锟叫憋拷
    	this.storyList = storyList;
    	
    	// 锟斤拷锟斤拷全锟斤拷锟斤拷锟斤拷锟叫憋拷
    	this.storyBook = this.storyList;
    	
    	// 锟斤拷锟矫癸拷锟斤拷锟叫憋拷页锟斤拷锟斤拷锟斤拷
    	this.updateStoryList(this.storyList);
    	
    }
    
    
    /**
     * 锟斤拷锟脚癸拷锟斤拷
     * @param storyId
     */
    private void playStory(int storyId){
    	
    	MediaPlayerHandler.getInstance().playStoryById(storyId);
    	
    }
    
    
    /**
     * 锟斤拷锟铰癸拷锟斤拷锟叫憋拷
     * @param newStoryList
     */
    private void updateStoryList(List<StoryItem> newStoryList){
    	// 锟斤拷锟铰诧拷锟斤拷页锟斤拷墓锟斤拷锟斤拷斜锟�
    	this.storyList = newStoryList;
    	
    	// 同锟斤拷锟斤拷锟铰诧拷锟斤拷锟斤拷锟侥癸拷锟斤拷锟叫憋拷
    	MediaPlayerHandler.getInstance().updateStoryList(this.storyList);
    	
    	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
    	
    	for(StoryItem storyItem:this.storyList){
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("story_item_title", storyItem.getTitle());
    		listMap.add(map);
    	}
    
    	
    	// 锟斤拷锟矫癸拷锟斤拷锟叫憋拷页锟斤拷锟斤拷锟斤拷
    	this.lvStoryList.setAdapter(
    			new SimpleAdapter(
    					StoryHome.this
    					, listMap
    					, R.layout.story_item
    					, new String[]{"story_item_title"} 
    					, new int[]{ R.id.story_item_title}
    					)
    			);
    	
    	// 锟斤拷锟矫癸拷锟斤拷锟斤拷目锟斤拷锟斤拷录锟�
    	this.setStoryOnclickEvent();
    	
    }
    
    
    
    /**
     * 锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷钮锟斤拷锟斤拷录锟�
     * @param view
     */
    public void playNextOnclick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playNextStory();
    }

    
    /**
     * 锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷钮锟斤拷锟斤拷录锟�
     * @param view
     */
    public void playForwardStoryOnClick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playForwardStory();
    }
    
    
    /**
     * 锟斤拷锟斤拷锟斤拷停锟斤拷锟斤拷锟脚硷拷
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
     * 锟斤拷锟铰诧拷锟斤拷状态
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
     * 锟斤拷锟矫癸拷锟铰碉拷锟斤拷录锟�
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
     * 锟斤拷取锟斤拷锟斤拷锟叫憋拷
     * @return
     */
    private List<StoryItem> getStoryListFromDisk(){
    	List<StoryItem> storyList = new ArrayList<StoryItem>();
    	Cursor storyCursors = this.searchWithPath();
    	Log.e("hah", "锟杰癸拷锟叫癸拷锟斤拷:" + storyCursors.getCount());
    	storyCursors.moveToFirst();
    	
    	for(int i  = 0; i < storyCursors.getCount(); ++i){
    		StoryItem storyItem = new StoryItem();
    		// 锟斤拷锟矫癸拷锟铰憋拷锟斤拷
    		String storyTitle = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.TITLE));
    		// 锟斤拷锟矫癸拷锟铰存储锟斤拷址
    		String storyLocation = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.DATA));
    		
    		storyItem.setTitle(storyTitle);
    		storyItem.setLocation(storyLocation);
    		
    		// 锟狡讹拷锟斤拷锟斤拷一锟斤拷
    		storyCursors.moveToNext();
    		
    		// 锟斤拷拥锟斤拷锟斤拷锟斤拷斜锟�
    		storyList.add(storyItem);
    	}
    	
    	return storyList;
    }
    
    
    
    /**
     * 锟斤拷取锟街伙拷music目录锟铰碉拷锟斤拷锟斤拷锟侥硷拷
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
	 * 锟斤拷锟斤拷锟斤拷锟侥癸拷锟斤拷锟斤拷聘锟斤拷鹿锟斤拷锟斤拷斜锟�
	 * @param needleStoryTitle
	 */
	private void updateStoryListByInputStoryTitle(String needleStoryTitle){
		
		List<StoryItem> hintStoryList = StoryListHandler.getMostSimilarStorys(this.storyBook, needleStoryTitle);
		
		
		this.updateStoryList(hintStoryList);
	}
	
	
	
	/**
	 * 锟斤拷锟矫百讹拷锟斤拷锟斤拷锟皆伙拷锟斤拷
	 * @param view
	 */
	public void invokeBaiduVoice(View view){
		
		// 锟斤拷锟斤拷锟斤拷诓锟斤拷殴锟斤拷锟�, 锟斤拷锟斤拷停止锟斤拷前锟侥诧拷锟脚ｏ拷锟斤拷锟斤拷影锟斤拷锟斤拷锟斤拷识锟斤拷慕锟斤拷
		if(MediaPlayerHandler.getInstance().isPlaying()){
			this.playOrPauseOnclick(null);
		}
		
        if (mDialog != null) {
            mDialog.dismiss();
        }
		
		Bundle params= new Bundle();
		
		//锟斤拷锟矫匡拷锟斤拷 API Key 
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 
		
		//锟斤拷锟矫匡拷锟斤拷平台 Secret Key 
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 
		
		//锟斤拷锟斤拷识锟斤拷锟斤拷锟斤拷:锟斤拷锟斤拷锟斤拷锟斤拷锟诫、锟斤拷图锟斤拷锟斤拷锟斤拷......,锟斤拷选锟斤拷默锟斤拷为锟斤拷锟诫。
//		params.putInt( BaiduASRDigitalDialog.PARAM_PROP, VoiceRecognitionConfig.PROP_INPUT);
		
		//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷:锟斤拷锟斤拷锟斤拷通锟斤拷,锟斤拷锟斤拷锟斤拷锟斤拷,英锟斤拷,锟斤拷选锟斤拷默锟斤拷为锟斤拷锟斤拷锟斤拷通锟斤拷
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
		
		//锟斤拷锟斤拷锟揭拷锟斤拷锟斤拷锟斤拷,锟斤拷锟斤拷锟铰凤拷锟斤拷锟斤拷锟斤拷锟斤拷为锟斤拷锟诫不支锟斤拷 
//		params.putBoolean(BaiduASRDigitalDialog.PARAM_NLU_ENABLE, true);
		
		// 锟斤拷锟矫对伙拷锟斤拷锟斤拷锟斤拷,锟斤拷选锟斤拷BaiduASRDigitalDialog 锟结供锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届、锟教★拷锟斤拷锟斤拷锟斤拷锟斤拷色,每锟斤拷锟斤拷 色锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷色锟斤拷锟斤拷
		// 锟斤拷 8 锟斤拷锟斤拷锟斤拷,锟斤拷锟斤拷锟竭匡拷锟皆帮拷锟斤拷选锟斤拷,取值锟轿匡拷 BaiduASRDigitalDialog 锟斤拷 前缀为 THEME_锟侥筹拷锟斤拷锟斤拷默锟斤拷为锟斤拷锟斤拷色 
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);
		
		mDialog = new BaiduASRDigitalDialog(this,params);
		mDialog.setDialogRecognitionListener(this.mRecognitionListener);

		mDialog.show();		
		
	}
	
	
	/**
	 * 锟接凤拷锟斤拷锟斤拷锟斤拷取锟斤拷锟斤拷锟斤拷源
	 */
	private void getStoryListFromServer()
	{
		// 锟斤拷锟酵碉拷陆锟斤拷锟斤拷
		ApiClent.getStoryList("", new ClientCallback() {
						
			// 锟缴癸拷
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
			
			// 失锟斤拷
			public void onFailure(String message) {
				Log.e(ConstantsCommon.LOG_TAG, message);
			}
			
			// 锟斤拷锟斤拷
			public void onError(Exception e) {
				Log.e(ConstantsCommon.LOG_TAG, e.getMessage());
			}
		});
	}
		

}
