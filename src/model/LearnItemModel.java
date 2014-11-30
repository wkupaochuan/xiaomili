package model;

public class LearnItemModel {
	// 音频文件的网络地址
	private String mediaUrl;
	// 音频文件对应的文字
	private String mediaText;
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getMediaText() {
		return mediaText;
	}
	public void setMediaText(String mediaText) {
		this.mediaText = mediaText;
	}

}
