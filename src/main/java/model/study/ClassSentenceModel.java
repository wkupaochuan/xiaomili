package model.study;

public class ClassSentenceModel {

    private int sentenceId;
	// 音频可播放地址
	private String mediaUrl;
	// 音频对应的文字
	private String mediaText;


    public void setSentenceId(int id)
    {
        this.sentenceId = id;
    }

    public int getSentenceId()
    {
        return this.sentenceId;
    }

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
