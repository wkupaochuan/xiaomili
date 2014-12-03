package model;

/**
 * ������Ŀ
 * 1-- �������һ������
 * @author Gain
 */

public class StoryItem {
	
	// ���µı���
	private String title;
	
	// ���´�ŵĵ�ַ(�����Ǳ��ص�ַ������Ҳ������������õ�ַ)
	private String location;
	
	/**
	 * ������췽��(ֱ��set��������, ���Ƽ�)
	 * @param title
	 * @param location
	 */
	public StoryItem(String title, String location){
		this.title = title;
		this.location = location;
	}
	
	/**
	 * �޲����췽��(ʵ��������set����)
	 */
	public StoryItem(){
		
	}


/*******************************˽�����Ե�get & set ����************************************************/
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
	 * ���ù��´�ŵ�ַ
	 * @param location
	 */
	public void setLocation(String location) {
		// У���ַ����Ч��
		if(this.isLegalLocation(location)){
			this.location = location;
		}
		else{
		}
	}
	


	private boolean isLegalLocation(String location){
		boolean result = true;
//		File testFile = new File(location);
//		if(!testFile.exists()){
//			result = false;
//		}
		return result;
	}
	
	

}
