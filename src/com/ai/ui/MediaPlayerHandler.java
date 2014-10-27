package com.ai.ui;

import java.util.ArrayList;
import java.util.List;
import android.media.MediaPlayer;

/**
 * 多媒体处理类
 *
 */
public class MediaPlayerHandler {
	
	// 正在播放状态
	public static final int PLAYING_STATUS_PLAYING = 1;
	
	// 正在暂停状态
	public static final int PLAYING_STATUS_PAUSE = 0;
	
	// 正在停止状态
	public static final int PLAYING_STATUS_STOP = -1;
	
	// 当前播放的故事id，为-1，表示当前列表里没有故事在播放
	public static final int ALIVE_STORY_ID_NULL = -1;
	
	// 故事列表
	private List<StoryItem> storyList = new ArrayList<StoryItem>();
	
	// 当前播放的故事id(-1代表目前暂无播放)
	private int aliveStoryId ;
	
	// 当前的播放状态
	private int playingStatus;

	// 单例
	private static MediaPlayerHandler instance;
	
	// 多媒体对象
	MediaPlayer mediaPlayer;
	
	/**
	 * 设置播放器的故事列表
	 * @param storyList
	 */
	public void setStoryList(List<StoryItem> storyList){
		this.storyList = storyList;
	}
	
	/**
	 * 更新播放器的故事列表
	 * @param storyList
	 */
	public void updateStoryList(List<StoryItem> storyList){
		this.storyList = storyList;
		
		// 同步更新当前播放故事为空(故事列表已经更新，所以原有id不再生效)
		this.updateAliveSotoryId(-1);
	}
	

	/**
	 * 检查是否在播放状态
	 * @return
	 */
	public boolean isPlaying(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_PLAYING;
	}
	
	/**
	 * 检查是否在暂停状态
	 * @return
	 */
	public boolean isPaused(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_PAUSE;
	}
	
	
	/**
	 * 检查是否在停止状态
	 * @return
	 */
	public boolean isStoped(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_STOP;
	}
	

	
	/**
	 * 开始播放某个指定的故事
	 */
	public void playStoryById(int storyId){
		
		// TODO 没有故事可以播放
		if(this.storyList.size() == 0){
			
		}
		
		// TODO 指定的故事不在当前列表中, 暂且处理为播放第一个故事，没有提示
		if(storyId < 0 || storyId >= this.storyList.size()){
			storyId = 0;
		}
		
    	// 获取故事条目
    	StoryItem storyItem = this.storyList.get(storyId);
    	
    	// 更新当前播放的故事id
    	this.updateAliveSotoryId(storyId);
    	
    	// 更新当前故事播放状态为“正在播放”
    	this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PLAYING;
		
		try {
			// 释放原有资源，重新播放
			this.mediaPlayer.reset();
			this.mediaPlayer.setDataSource(storyItem.getLocation());
			this.mediaPlayer.prepare();
			this.mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 播放
	 */
	public void play(){
		if(this.isStoped()){
			this.playStoryById(this.aliveStoryId);
		}
	}

	
	/**
	 * 暂停播放
	 */
	public void pause(){
		// 如果正在播放，则处理为暂停播放
		if(this.isPlaying()){
			// 暂停
			this.mediaPlayer.pause();
			
			// 更新播放状态为暂停
			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PAUSE;
		}
	}
	
	/**
	 * 继续播放
	 */
	public void continutePlaying(){
		// 如果正在播放，则处理为暂停播放
		if(this.isPaused()){
			// 继续播放
			this.mediaPlayer.start();
			
			// 更新播放状态为正在播放
			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PLAYING;
		}
	}
	
    /**
     * 播放下一个故事
     */
    public  void playNextStory(){
    	// 初次播放
    	if(MediaPlayerHandler.ALIVE_STORY_ID_NULL == this.aliveStoryId){
    		this.playStoryById(0);
    	}
    	// 列表末尾
    	else if(this.aliveStoryId == (this.storyList.size() - 1) ){
    		this.playStoryById(0);
    	}
    	else{
    		this.playStoryById(this.aliveStoryId + 1);
    	}
    }
    
    
    
    /**
     * 播放上一个故事
     */
    public void playForwardStory(){
    	// 初次播放
    	if(this.aliveStoryId == MediaPlayerHandler.ALIVE_STORY_ID_NULL){
    		this.playStoryById(0);
    	}
    	// 第一首
    	else if(this.aliveStoryId == 0){
    		this.playStoryById(this.storyList.size() - 1);
    	}
    	else{
    		this.playStoryById(this.aliveStoryId - 1);
    	}	
    }

	
	/**
	 * 单例模式
	 */
	public static MediaPlayerHandler getInstance(){
		if(null == instance)
		{
			instance = new MediaPlayerHandler();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	public MediaPlayerHandler(){
		if(null == this.mediaPlayer){
			// 实例化播放器
			this.mediaPlayer = new MediaPlayer();
			
			// 设置播放完毕事件
			this.setOnCompletionListener();
			
			// 设置当前播放状态(停止)
			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_STOP;
			
			// 设置当前播放的故事id(暂无播放)
			this.aliveStoryId = -1;
		}
	}
	
	
	/**
	 * 设置单个音频播放完毕事件
	 */
	private void setOnCompletionListener(){
		this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        
	        public void onCompletion(MediaPlayer arg0) {
	        	playNextStory();
	        }
		});
	}
	
	/**
	 * 更新正在播放的故事id
	 * @param storyId
	 */
	private void updateAliveSotoryId(int storyId){
		this.aliveStoryId = storyId;
	}
	

}
