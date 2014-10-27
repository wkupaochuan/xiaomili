package com.ai.ui;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;


/**
 * 下载线程类
 * @author jixiaofeng
 *
 */
class DownloadThread extends Thread{
	private int threadId;
	private String filePath;
	
	int fileBeginPosition;
	int fileEndPosition;
	
	/**
	 * 构造方法
	 * @param threadId
	 * @param filePath
	 * @param fileBeginPosition
	 * @param fileEndPosition
	 */
	public DownloadThread(int threadId, String filePath,
			int fileBeginPosition, int fileEndPosition) {
		Log.e("hah", "线程开始"+ this.threadId);
		this.threadId = threadId;
		this.filePath = filePath;
		this.fileBeginPosition = fileBeginPosition;
		this.fileEndPosition = fileEndPosition;
	}


	
	public void run() {
		try {
			//Log.e("hah", "线程开始"+ this.threadId);
			URL url = new URL(this.filePath);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestProperty("Range", "bytes=" + this.fileBeginPosition + "-" + this.fileEndPosition);
			
			conn.setRequestMethod("GET");
			
			conn.setConnectTimeout(10000);
			
			//int responseCode = conn.getResponseCode();
			
			
//			if(responseCode == 200){
				
				InputStream is = conn.getInputStream();
				
				
				RandomAccessFile file = new RandomAccessFile( DownloadService.getLocalFilePath(filePath), "rwd");
				
//				RandomAccessFile file = new RandomAccessFile(DownLoadMain.getFileName(filePath), "rwd");
			
				file.seek(this.fileBeginPosition);
				
				byte[] buffer = new byte[1024];
				
				int len = 0;
				while((len = is.read(buffer)) != -1){
					file.write(buffer, 0, len);
				}
				
				file.close();
				
				Log.e("hah", "线程" + this.threadId + "下载完毕; 开始位置:" + this.fileBeginPosition + "; 结束位置:" + this.fileEndPosition);

//			}
			
			
		} catch (Exception e) {
			Log.e("hah", e.getMessage());
		}
	}
	
	
	
}
