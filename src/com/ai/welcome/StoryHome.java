package com.ai.welcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class StoryHome extends ActionBarActivity{
	
	
	// �����б���
	private ListView lvStoryList;
	
	// �����б�
	
	
	
	
	
	/**
	 * ��ʼ��ҳ��
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_home);
        
        this.initStoryListView();
    }
    
    
    
    /**
     * ��ʼ�������б�
     */
    private void initStoryListView(){
    	// ��ʼ�������б���
    	this.lvStoryList = (ListView) this.findViewById(R.id.lv_music_list);
    	
    	// ��ȡ�����б�
    	List<Map<String, String>> storyList = this.getStoryList();
    	
    	// ���ù����б�ҳ������
    	this.lvStoryList.setAdapter(
    			new SimpleAdapter(
    					StoryHome.this
    					, storyList
    					, R.layout.story_item
    					, new String[]{"story_item_id", "story_item_title"} 
    					, new int[]{R.id.story_item_id, R.id.story_item_title}
    					)
    			);
    }
    
    /**
     * ��ȡ�����б�
     * @return
     */
    private List<Map<String, String>> getStoryList(){
    	Cursor storyCursors = this.searchWithPath();
    	storyCursors.moveToFirst();
    	List<Map<String, String>> storyList = new ArrayList<Map<String, String>>();
    	for(int i  = 0; i < storyCursors.getCount(); ++i){
    		HashMap<String, String> map = new HashMap<String, String>();
    		// ���ù��±��
    		map.put("story_item_id", "" + i); 
    		
    		// ���ù��±���
    		String storyTitle = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.TITLE));
    		if(storyTitle.length() > 40){
    			storyTitle = storyTitle.substring(0, 40);
    		}
    		map.put("story_item_title", storyTitle);
    		
    		// ���ù��´洢��ַ
    		String storyLocation = storyCursors.getString(storyCursors.getColumnIndex(MediaStore.Audio.Media.DATA));
    		map.put("location", storyLocation);
    		
    		storyCursors.moveToNext();
    		storyList.add(map);
    	}
    	
    	return storyList;
    }
    
    
    
    /**
     * ��ȡ�ֻ�musicĿ¼�µ������ļ�
     * @param path
     */
	private Cursor searchWithPath() {
		String path = "/storage/emulated/0/Music";
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

}
