package tools;

import android.media.MediaPlayer;


/**
 * 全局变量mediaPlayer
 */
public class XMediaPlayer {
	
	private static  MediaPlayer instance;

    /**
     * 获取播放器实例
     * @return
     */
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
        MediaPlayer mp = XMediaPlayer.getInstance();
        mp.reset();
		try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * 暂停播放
     */
    public static void pause()
    {
        MediaPlayer mp = XMediaPlayer.getInstance();
        if(mp.isPlaying())
        {
            mp.pause();
        }
    }


    /**
     * 继续播放
     */
    public static void goOn()
    {
        MediaPlayer mp = XMediaPlayer.getInstance();

    }

}
