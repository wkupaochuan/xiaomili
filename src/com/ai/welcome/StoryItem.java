package com.ai.welcome;

import java.io.File;

/**
 * 故事条目
 * 1-- 用来代表一条故事
 * @author jixiaofeng
 */

public class StoryItem {
	
	// 故事的标题
	private String title;
	
	// 故事存放的地址(可能是本地地址，将来也可能是网络可用地址)
	private String location;
	
	/**
	 * 带参数构造方法(直接set所有属性, 不推荐)
	 * @param title
	 * @param location
	 */
	public StoryItem(String title, String location){
		this.title = title;
		this.location = location;
	}
	
	/**
	 * 无参数构造方法(实例化后，在逐个set属性)
	 */
	public StoryItem(){
		
	}

	
	
	
	
	
	
	
/*******************************私有属性的get & set 方法************************************************/	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	/**
	 * 设置故事存放地址
	 * @param location
	 */
	public void setLocation(String location) {
		// 校验地址的有效性
		if(this.isLegalLocation(location)){
			this.location = location;
		}
		else{
			//TODO 遇到不合法地址如何处理
		}
	}
	
	
	/**
	 * 校验某个地址是否合法
	 * @param location
	 * @return
	 */
	private boolean isLegalLocation(String location){
		boolean result = true;
		File testFile = new File(location);
		if(!testFile.exists()){
			result = false;
		}
		return result;
	}
	
	

}
