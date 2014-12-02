package service.story;

import java.util.ArrayList;
import java.util.List;

import model.StoryItem;
import android.media.MediaPlayer;

/**
 * ��ý�崦����
 *
 */
public class MediaPlayerHandler {
	
	// ���ڲ���״̬
	public static final int PLAYING_STATUS_PLAYING = 1;
	
	// ������ͣ״̬
	public static final int PLAYING_STATUS_PAUSE = 0;
	
	// ����ֹͣ״̬
	public static final int PLAYING_STATUS_STOP = -1;
	
	// ��ǰ���ŵĹ���id��Ϊ-1����ʾ��ǰ�б���û�й����ڲ���
	public static final int ALIVE_STORY_ID_NULL = -1;
	
	// �����б�
	private List<StoryItem> storyList = new ArrayList<StoryItem>();
	
	// ��ǰ���ŵĹ���id(-1���Ŀǰ���޲���)
	private int aliveStoryId ;
	
	// ��ǰ�Ĳ���״̬
	private int playingStatus;

	// ����
	private static MediaPlayerHandler instance;
	
	// ��ý�����
	MediaPlayer mediaPlayer;
	
	/**
	 * ���ò������Ĺ����б�
	 * @param storyList
	 */
	public void setStoryList(List<StoryItem> storyList){
		this.storyList = storyList;
	}
	
	/**
	 * ���²������Ĺ����б�
	 * @param storyList
	 */
	public void updateStoryList(List<StoryItem> storyList){
		this.storyList = storyList;
		
		// ͬ�����µ�ǰ���Ź���Ϊ��(�����б��Ѿ����£�����ԭ��id������Ч)
		this.updateAliveSotoryId(-1);
	}
	

	/**
	 * ����Ƿ��ڲ���״̬
	 * @return
	 */
	public boolean isPlaying(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_PLAYING;
	}
	
	/**
	 * ����Ƿ�����ͣ״̬
	 * @return
	 */
	public boolean isPaused(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_PAUSE;
	}
	
	
	/**
	 * ����Ƿ���ֹͣ״̬
	 * @return
	 */
	public boolean isStoped(){
		return this.playingStatus == MediaPlayerHandler.PLAYING_STATUS_STOP;
	}
	

	
	/**
	 * ��ʼ����ĳ��ָ���Ĺ���
	 */
	public void playStoryById(int storyId){
		
		// TODO û�й��¿��Բ���
		if(this.storyList.size() == 0){
			
		}
		
		// TODO ָ���Ĺ��²��ڵ�ǰ�б���, ���Ҵ���Ϊ���ŵ�һ�����£�û����ʾ
		if(storyId < 0 || storyId >= this.storyList.size()){
			storyId = 0;
		}
		
    	// ��ȡ������Ŀ
    	StoryItem storyItem = this.storyList.get(storyId);
    	
    	// ���µ�ǰ���ŵĹ���id
    	this.updateAliveSotoryId(storyId);
    	
    	// ���µ�ǰ���²���״̬Ϊ�����ڲ��š�
    	this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PLAYING;
		
		try {
			// �ͷ�ԭ����Դ�����²���
			this.mediaPlayer.reset();
			this.mediaPlayer.setDataSource(storyItem.getLocation());
			this.mediaPlayer.prepare();
			this.mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * ����
	 */
	public void play(){
		if(this.isStoped()){
			this.playStoryById(this.aliveStoryId);
		}
	}

	
	/**
	 * ��ͣ����
	 */
	public void pause(){
		// ������ڲ��ţ�����Ϊ��ͣ����
		if(this.isPlaying()){
			// ��ͣ
			this.mediaPlayer.pause();
			
			// ���²���״̬Ϊ��ͣ
			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PAUSE;
		}
	}
	
	/**
	 * �����
	 */
	public void continutePlaying(){
		// ������ڲ��ţ�����Ϊ��ͣ����
		if(this.isPaused()){
			// �����
			this.mediaPlayer.start();
			
			// ���²���״̬Ϊ���ڲ���
			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_PLAYING;
		}
	}
	
    /**
     * ������һ������
     */
    public  void playNextStory(){
    	// ���β���
    	if(MediaPlayerHandler.ALIVE_STORY_ID_NULL == this.aliveStoryId){
    		this.playStoryById(0);
    	}
    	// �б�ĩβ
    	else if(this.aliveStoryId == (this.storyList.size() - 1) ){
    		this.playStoryById(0);
    	}
    	else{
    		this.playStoryById(this.aliveStoryId + 1);
    	}
    }
    
    
    
    /**
     * ������һ������
     */
    public void playForwardStory(){
    	// ���β���
    	if(this.aliveStoryId == MediaPlayerHandler.ALIVE_STORY_ID_NULL){
    		this.playStoryById(0);
    	}
    	// ��һ��
    	else if(this.aliveStoryId == 0){
    		this.playStoryById(this.storyList.size() - 1);
    	}
    	else{
    		this.playStoryById(this.aliveStoryId - 1);
    	}	
    }

	
	/**
	 * ����ģʽ
	 */
	public static MediaPlayerHandler getInstance(){
		if(null == instance)
		{
			instance = new MediaPlayerHandler();
		}
		return instance;
	}

	/**
	 * ���췽��
	 */
	public MediaPlayerHandler(){
		if(null == this.mediaPlayer){
			// ʵ�����
			this.mediaPlayer = new MediaPlayer();
			
			// ���ò�������¼�
			this.setOnCompletionListener();
			
			// ���õ�ǰ����״̬(ֹͣ)
			this.playingStatus = MediaPlayerHandler.PLAYING_STATUS_STOP;
			
			// ���õ�ǰ���ŵĹ���id(���޲���)
			this.aliveStoryId = -1;
		}
	}
	
	
	/**
	 * ���õ�����Ƶ��������¼�
	 */
	private void setOnCompletionListener(){
		this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        
	        public void onCompletion(MediaPlayer arg0) {
	        	playNextStory();
	        }
		});
	}
	
	/**
	 * �������ڲ��ŵĹ���id
	 * @param storyId
	 */
	private void updateAliveSotoryId(int storyId){
		this.aliveStoryId = storyId;
	}
	

}
