package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.ClientCallBack;
import model.StoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.story.MediaPlayerHandler;
import service.story.StoryListHandler;

import constants.ConstantsCommon;
import api.ApiClent;
import tools.BaiduVoice;

import com.ai.welcome.R;
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

public class Story extends BaseActivity{


	private ListView lvStoryList;


	private List<StoryItem> storyList = new ArrayList<StoryItem>();


	private List<StoryItem> storyBook = new ArrayList<StoryItem>();


    private BaiduASRDigitalDialog mDialog = null;


    private DialogRecognitionListener mRecognitionListener;


    private ImageButton playOrPauseButton;
    


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_home);


        //this.initStoryListView();

        this.initBiduVoiceRecgnitionListener();


        this.playOrPauseButton = (ImageButton) this.findViewById(R.id.btn_play_or_pause);


        this.getStoryListFromServer();
    }


    private void initBiduVoiceRecgnitionListener(){
    	
		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){

				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;
				
				if(rs != null && rs.size() > 0){

                    for(String str:rs){
						Log.e("hah", "锟斤拷说锟斤拷锟斤拷:" + str.toString());
					}


					if(MediaPlayerHandler.getInstance().isPaused()){
						playOrPauseOnclick(null);
					}


					updateStoryListByInputStoryTitle(rs.get(0));
					
				}
				
			}
            
		};
    }


    private void initStoryListView(List<StoryItem> storyList){

    	this.lvStoryList = (ListView) this.findViewById(R.id.lv_music_list);


    	this.storyList = storyList;


    	this.storyBook = this.storyList;


    	this.updateStoryList(this.storyList);
    	
    }
    


    private void playStory(int storyId){
    	MediaPlayerHandler.getInstance().playStoryById(storyId);
    }
    
    
    /**
     * 锟斤拷锟铰癸拷锟斤拷锟叫憋拷
     * @param newStoryList
     */
    private void updateStoryList(List<StoryItem> newStoryList){

    	this.storyList = newStoryList;
    	

    	MediaPlayerHandler.getInstance().updateStoryList(this.storyList);
    	
    	List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
    	
    	for(StoryItem storyItem:this.storyList){
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("story_item_title", storyItem.getTitle());
    		listMap.add(map);
    	}

    	this.lvStoryList.setAdapter(
    			new SimpleAdapter(
    					Story.this
    					, listMap
    					, R.layout.story_item
    					, new String[]{"story_item_title"} 
    					, new int[]{ R.id.story_item_title}
    					)
    			);

    	this.setStoryOnclickEvent();
    	
    }
    

    public void playNextOnclick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playNextStory();
    }



    public void playForwardStoryOnClick(View view){
    	this.updatePlayStatus(MediaPlayerHandler.PLAYING_STATUS_PAUSE);
    	MediaPlayerHandler.getInstance().playForwardStory();
    }


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


    private void updatePlayStatus(int playingStatus)
    {
    	if(playingStatus == MediaPlayerHandler.PLAYING_STATUS_PAUSE){
    		this.playOrPauseButton.setBackgroundResource(R.drawable.pause_selecor);
    	}
    	else if(playingStatus == MediaPlayerHandler.PLAYING_STATUS_PLAYING){
    		this.playOrPauseButton.setBackgroundResource(R.drawable.play_selecor);
    	}
    }
    


    private void setStoryOnclickEvent(){
    	
    	this.lvStoryList.setOnItemClickListener(new OnItemClickListener(){
    		
    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
    			playStory((int)id);
			}
    		
    	});
    }


    public List<StoryItem> getStoryListFromDisk(){
    	List<StoryItem> storyList = new ArrayList<StoryItem>();
    	Cursor storyCursors = this.searchWithPath();
    	Log.e("hah", "锟杰癸拷锟叫癸拷锟斤拷:" + storyCursors.getCount());
    	storyCursors.moveToFirst();
    	
    	for(int i  = 0; i < storyCursors.getCount(); ++i){
    		StoryItem storyItem = new StoryItem();

    		String storyTitle = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.TITLE));

    		String storyLocation = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.DATA));
    		
    		storyItem.setTitle(storyTitle);
    		storyItem.setLocation(storyLocation);

    		storyCursors.moveToNext();

    		storyList.add(storyItem);
    	}
    	
    	return storyList;
    }
    

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
	

	private void updateStoryListByInputStoryTitle(String needleStoryTitle){
		
		List<StoryItem> hintStoryList = StoryListHandler.getMostSimilarStorys(this.storyBook, needleStoryTitle);
		
		
		this.updateStoryList(hintStoryList);
	}
	

	public void invokeBaiduVoice(View view){

		if(MediaPlayerHandler.getInstance().isPlaying()){
			this.playOrPauseOnclick(null);
		}

        mDialog = BaiduVoice.initBaiduVoiceDialog(this);

		mDialog.show();		
		
	}

	private void getStoryListFromServer()
	{

		ApiClent.getStoryList("", new ClientCallBack() {

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
					
					Story.this.initStoryListView(storyList);
				} catch (JSONException e) {
				}
			}


			public void onFailure(String message) {
				Log.e(ConstantsCommon.LOG_TAG, message);
			}

		});
	}
		

}
