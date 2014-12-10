package service.story;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

import model.StoryItem;


public class MediaPlayerHandler {

	public static final int PLAYING_STATUS_PLAYING = 1;

	public static final int PLAYING_STATUS_PAUSE = 0;

	public static final int PLAYING_STATUS_STOP = -1;

	public static final int ALIVE_STORY_ID_NULL = -1;

	private List<StoryItem> storyList = new ArrayList<StoryItem>();

	private int aliveStoryId ;

	private int playingStatus;

	private static MediaPlayerHandler instance;
	

	MediaPlayer mediaPlayer;


	public void setStoryList(List<StoryItem> storyList){
		this.storyList = storyList;
	}


	public void updateStoryList(List<StoryItem> storyList){
		this.storyList = storyList;


		this.updateAliveSotoryId(-1);
	}
	


	public boolean isPlaying(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_PLAYING;
	}


	public boolean isPaused(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_PAUSE;
	}
	


	public boolean isStoped(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_STOP;
	}
	



	public void playStoryById(int storyId){


		if(this.storyList.size() == 0){
			
		}


		if(storyId < 0 || storyId >= this.storyList.size()){
			storyId = 0;
		}


    	StoryItem storyItem = this.storyList.get(storyId);


    	this.updateAliveSotoryId(storyId);


    	this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PLAYING;
		
		try {

			this.mediaPlayer.reset();
			this.mediaPlayer.setDataSource(storyItem.getLocation());
			this.mediaPlayer.prepare();
			this.mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void play(){
		if(this.isStoped()){
			this.playStoryById(this.aliveStoryId);
		}
	}


	public void pause(){

		if(this.isPlaying()){

			this.mediaPlayer.pause();
			

			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PAUSE;
		}
	}


	public void continutePlaying(){

		if(this.isPaused()){

			this.mediaPlayer.start();


			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PLAYING;
		}
	}


    public  void playNextStory(){

    	if(MediaPlayerHandler.ALIVE_STORY_ID_NULL == this.aliveStoryId){
    		this.playStoryById(0);
    	}

    	else if(this.aliveStoryId == (this.storyList.size() - 1) ){
    		this.playStoryById(0);
    	}
    	else{
    		this.playStoryById(this.aliveStoryId + 1);
    	}
    }
    
    


    public void playForwardStory(){

    	if(this.aliveStoryId == MediaPlayerHandler.ALIVE_STORY_ID_NULL){
    		this.playStoryById(0);
    	}

    	else if(this.aliveStoryId == 0){
    		this.playStoryById(this.storyList.size() - 1);
    	}
    	else{
    		this.playStoryById(this.aliveStoryId - 1);
    	}	
    }



	public static MediaPlayerHandler getInstance(){
		if(null == instance)
		{
			instance = new MediaPlayerHandler();
		}
		return instance;
	}


	public MediaPlayerHandler(){
		if(null == this.mediaPlayer){

			this.mediaPlayer = new MediaPlayer();


			this.setOnCompletionListener();


			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_STOP;

			this.aliveStoryId = -1;
		}
	}
	


	private void setOnCompletionListener(){
		this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        
	        public void onCompletion(MediaPlayer arg0) {
	        	playNextStory();
	        }
		});
	}


	private void updateAliveSotoryId(int storyId){
		this.aliveStoryId = storyId;
	}
	

}
