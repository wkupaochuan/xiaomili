package tools;

import android.media.MediaPlayer;

/**
 * ȫ��Ψһ����Ƶ������
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
     * ����
     * @param path
     */
    public static void play(String path)
    {
    	// �ͷ�ԭ����Դ�����²���
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
