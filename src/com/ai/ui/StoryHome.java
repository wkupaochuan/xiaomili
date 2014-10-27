package com.ai.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ai.welcome.R;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import config.ApiClent;
import config.ApiClent.ClientCallback;

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
	
	// 故事列表布局
	private ListView lvStoryList;
	
	// 故事列表
	private List<StoryItem> storyList = new ArrayList<StoryItem>();
	
	// 全部故事集合(每次都从这里面查询，然后赋值给storyList)
	private List<StoryItem> storyBook = new ArrayList<StoryItem>();
	
	// 百度语音识别对话框
    private BaiduASRDigitalDialog mDialog = null;

    // 百度语音识别结果监听器
    private DialogRecognitionListener mRecognitionListener;
    
    // 播放、暂停按钮
    private ImageButton playOrPauseButton;
    
	
	/**
	 * 初始化页面
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_home);
        
        // 设置故事列表布局
        //this.initStoryListView();
        

        // 初始化百度语音识别结果监听器
        this.initBiduVoiceRecgnitionListener();
        
        // 初始化播放按钮
        this.playOrPauseButton = (ImageButton) this.findViewById(R.id.btn_play_or_pause);
        
        // 从服务器获取故事资源
        this.getStoryListFromServer();
    }
    
    
    /**
     * 初始化百度语音识别结果监听器
     */
    private void initBiduVoiceRecgnitionListener(){
    	
		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){
			//在 Results 中获取 Key 为 DialogRecognitionListener .RESULTS_RECOGNITION 的 StringArrayList
			// ,可能为空。获取到识别结果后执行相应的业务逻辑即可,此回调会在主线程调用。 
				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;
				
				if(rs != null && rs.size() > 0){
					//mDialog.dismiss();
					//此处处理识别结果,识别结果可能有多个,按置信度从高到低排列,第一个元素是置信度最高的结果。
					for(String str:rs){
						Log.e("hah", "你说的是:" + str.toString());
					}
					
					// 识别完毕，继续播放
					if(MediaPlayerHandler.getInstance().isPaused()){
						playOrPauseOnclick(null);
					}
					
					// 根据识别结果更新故事列表
					updateStoryListByInputStoryTitle(rs.get(0));
					
				}
				
			}
            
		};
    }
    
    
    /**
     * 初始化故事列表
     */
    private void initStoryListView(List<StoryItem> storyList){
    	// 初始化故事列表布局
    	this.lvStoryList = (ListView) this.findViewById(R.id.lv_music_list);
    	
    	// 获取故事列表
    	this.storyList = storyList;
    	
    	// 设置全部故事列表
    	this.storyBook = this.storyList;
    	
    	// 设置故事列表页面内容
    	this.updateStoryList(this.storyList);
    	
    }
    
    
    /**
     * 播放故事
     * @param storyId
     */
    private void playStory(int storyId){
    	
    	MediaPlayerHandler.getInstance().playStoryById(storyId);
    	
    }
    
    
    /**
     * 更新故事列表
     * @param newStoryList
     */
    private void updateStoryList(List<StoryItem> newStoryList){
    	// 更新播放页面的故事列表
    	this.storyList = newStoryList;
    	
    	// 同步更新播放器的故事列表
    	MediaPlayerHandler.getInstance().updateStoryList(this.storyList);
    	
    	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
    	
    	for(StoryItem storyItem:this.storyList){
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("story_item_title", storyItem.getTitle());
    		listMap.add(map);
    	}
    
    	
    	// 设置故事列表页面内容
    	this.lvStoryList.setAdapter(
    			new SimpleAdapter(
    					StoryHome.this
    					, listMap
    					, R.layout.story_item
    					, new String[]{"story_item_title"} 
    					, new int[]{ R.id.story_item_title}
    					)
    			);
    	
    	// 设置故事条目点击事件
    	this.setStoryOnclickEvent();
    	
    }
    
    
    
    /**
     * 监听“下一个”按钮点击事件
     * @param view
     */
    public void playNextOnclick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playNextStory();
    }

    
    /**
     * 监听“上一个”按钮点击事件
     * @param view
     */
    public void playForwardStoryOnClick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playForwardStory();
    }
    
    
    /**
     * 监听暂停、播放键
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
     * 更新播放状态
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
     * 设置故事点击事件
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
     * 获取故事列表
     * @return
     */
    private List<StoryItem> getStoryListFromDisk(){
    	List<StoryItem> storyList = new ArrayList<StoryItem>();
    	Cursor storyCursors = this.searchWithPath();
    	Log.e("hah", "总共有故事:" + storyCursors.getCount());
    	storyCursors.moveToFirst();
    	
    	for(int i  = 0; i < storyCursors.getCount(); ++i){
    		StoryItem storyItem = new StoryItem();
    		// 设置故事标题
    		String storyTitle = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.TITLE));
    		// 设置故事存储地址
    		String storyLocation = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.DATA));
    		
    		storyItem.setTitle(storyTitle);
    		storyItem.setLocation(storyLocation);
    		
    		// 移动到下一条
    		storyCursors.moveToNext();
    		
    		// 添加到故事列表
    		storyList.add(storyItem);
    	}
    	
    	return storyList;
    }
    
    
    
    /**
     * 读取手机music目录下的音乐文件
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
	 * 根据输入的故事名称更新故事列表
	 * @param needleStoryTitle
	 */
	private void updateStoryListByInputStoryTitle(String needleStoryTitle){
		
		List<StoryItem> hintStoryList = StoryListHandler.getMostSimilarStorys(this.storyBook, needleStoryTitle);
		
		for(StoryItem m:hintStoryList){
			Log.e("hah", m.getTitle());
		}
		
		this.updateStoryList(hintStoryList);
	}
	
	
	
	/**
	 * 调用百度语音对话框
	 * @param view
	 */
	public void invokeBaiduVoice(View view){
		
		// 如果正在播放故事, 首先停止当前的播放，以免影响语音识别的结果
		if(MediaPlayerHandler.getInstance().isPlaying()){
			this.playOrPauseOnclick(null);
		}
		
        if (mDialog != null) {
            mDialog.dismiss();
        }
		
		Bundle params= new Bundle();
		
		//设置开放 API Key 
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "7Ad10dy9IB1qpNPS1mwSh4Is"); 
		
		//设置开放平台 Secret Key 
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "NziRvQkD1SqA8TjYSyhxfrVnhUWbH9Lo"); 
		
		//设置识别领域:搜索、输入、地图、音乐......,可选。默认为输入。
//		params.putInt( BaiduASRDigitalDialog.PARAM_PROP, VoiceRecognitionConfig.PROP_INPUT);
		
		//设置语种类型:中文普通话,中文粤语,英文,可选。默认为中文普通话
		params.putString( BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
		
		//如果需要语义解析,设置下方参数。领域为输入不支持 
//		params.putBoolean(BaiduASRDigitalDialog.PARAM_NLU_ENABLE, true);
		
		// 设置对话框主题,可选。BaiduASRDigitalDialog 提供了蓝、暗、红、绿、橙四中颜色,每种颜 色又分亮、暗两种色调。
		// 共 8 种主题,开发者可以按需选择,取值参考 BaiduASRDigitalDialog 中 前缀为 THEME_的常量。默认为亮蓝色 
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_RED_DEEPBG);
		
		mDialog = new BaiduASRDigitalDialog(this,params);
		mDialog.setDialogRecognitionListener(this.mRecognitionListener);

		mDialog.show();		
		
	}
	
	
	/**
	 * 从服务器获取故事资源
	 */
	private void getStoryListFromServer()
	{
		// 发送登陆请求
		ApiClent.getStoryList("", new ClientCallback() {
						
			// 登陆成功
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
			
			// 登录失败
			public void onFailure(String message) {
			}
			
			// 登陆报错
			public void onError(Exception e) {
			}
		});
	}
		

}
