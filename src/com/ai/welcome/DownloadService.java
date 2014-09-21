package com.ai.welcome;


import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

public class DownloadService {

	public static final String filePath = "http://182.92.130.192:8080/test.mp3";
	

	public static void downlaod() {
		try {
//			Log.e("hah", "Environment.getDataDirectory()");
			//new File(Environment.getDataDirectory()+"/new.zip")
			
			Log.e("hah", "开始下载");
			URL url = new URL(filePath);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			conn.setConnectTimeout(10000);
			
			int fileSize = conn.getContentLength();
			
			
			RandomAccessFile file = new RandomAccessFile(getLocalFilePath(filePath), "rwd");
			
			file.setLength(fileSize);
			
			int threadNum = 3;
			file.close();
			
			int blockSize = fileSize/threadNum;
			
			/**
			 * t1 0 - blocksize
			 * t2 1*blocksize - 2*blockSize
			 * t3 2*blockSize - end
			 */
			
			for(int i = 0 ; i < threadNum; ++i){
				int beginPosition = i * blockSize;
				int endPosition = (i+1)*blockSize;
				if( i == (threadNum - 1)){
					endPosition = fileSize;
				}
				Log.e("hah", "开始下载" + i);
				DownloadThread dp = new DownloadThread(i, filePath, beginPosition, endPosition);
				dp.start();
			}
			
			
			
		} catch (Exception e) {
			Log.e("hah", e.getMessage());
		}
		
		
		
		
	}
	
	
	/**
	 * 获取文件名(根据path)
	 * @param filePath
	 * @return
	 */
	public static String getLocalFilePath(String filePath){
		return "/storage/emulated/0/Music/" + filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
	}
	
	
	
	
}





