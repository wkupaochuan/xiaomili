package tools;

import android.media.MediaPlayer;

/**
 * 全局唯一的音频播放器
 */
public class XMediaPlayer {
	
	private static  MediaPlayer instance;
	
	public static MediaPlayer getInstance()
	{
		if(instance == null)
		{
			instance = new MediaPlayer();
		}
		return instance;
	}
	
	
	 /**
     * 播放
     * @param path
     */
    public static void play(String path)
    {
    	// 释放原有资源，重新播放
    	XMediaPlayer.getInstance().reset();
		try {
			XMediaPlayer.getInstance().setDataSource(path);
			XMediaPlayer.getInstance().prepare();
			XMediaPlayer.getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
