package model.story;


public class StoryItem {

    // 故事名称
	private String title;
	
	// 故事播放地址
	private String location;

    /**
     * 构造方法
     * @param title
     * @param location
     */
	public StoryItem(String title, String location){
		this.title = title;
		this.location = location;
	}


	public StoryItem(){
		
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
        this.location = location;
	}

}
