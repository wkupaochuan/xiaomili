package service.story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.StoryItem;


public class StoryListHandler {
	
	private static String SIMILARITY_KEY = "similarity";
	
	
	
	/**
	 * ��ݹ�����ƣ����������ƵĹ����б�
	 * @param originalStoryList			�����б�ȫ��
	 * @param needleStoryTitle			Ŀ��������
	 * @return
	 */
	public static List<StoryItem> getMostSimilarStorys(List<StoryItem> originalStoryList, String needleStoryTitle ){
		
		List<Map<String, Object>> storyWithSimilarityPoint = new ArrayList<Map<String, Object>>();
		
		List<StoryItem> hintStoryList = new ArrayList<StoryItem>();
		
		
		// ��ÿ�����¼�����Ŀ����±�������ƶ�
		for(StoryItem storyItem: originalStoryList){
			Map<String, Object> xx = new HashMap<String, Object>();
			float similarity = getSimilarityOfTwoString(storyItem.getTitle(), needleStoryTitle);
			
			xx.put("story_item", storyItem);
			xx.put(SIMILARITY_KEY, similarity);
			
			storyWithSimilarityPoint.add(xx);
		}
		
		// ����(������ƶ�)
        Collections.sort(storyWithSimilarityPoint, new MapComparator());
        
        // ȡ�������Ƶ�ǰ3��
        int i = 0;
        for(Map<String, Object> m: storyWithSimilarityPoint){
        	hintStoryList.add((StoryItem)m.get("story_item"));
        	++i;
        	if(i > 3){
        		break;
        	}
		}
		
		return hintStoryList;
	}
	
	
	/**
	 * ���������ַ�����ƶ�
	 * @param str1			��һ���ַ�
	 * @param str2			�ڶ����ַ�
	 * Ӧ�ó���:DNA���� ����ƴ�ּ�� ����������ʶ ������Ϯ���
	 */
	public static float getSimilarityOfTwoString(String str1,String str2) {
		//���������ַ�ĳ��ȡ�
		int len1 = str1.length();
		int len2 = str2.length();
		//��������˵�����飬���ַ�ȴ�һ���ռ�
		int[][] dif = new int[len1 + 1][len2 + 1];
		//����ֵ������B��
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		//���������ַ��Ƿ�һ��������ϵ�ֵ
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				//ȡ���ֵ����С��
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
						dif[i - 1][j] + 1);
			}
		}
		System.out.println("�ַ�\""+str1+"\"��\""+str2+"\"�ıȽ�");
		//ȡ�������½ǵ�ֵ��ͬ��ͬλ�ô�?ͬ�ַ�ıȽ�
		System.out.println("���첽�裺"+dif[len1][len2]);
		//�������ƶ�
		float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
		return similarity;
		
	}
	
	
	
	/**
	 * �����в����еõ���Сֵ
	 * @param is
	 * @return
	 */
	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}
	
	
	
	/**
	 * �Ƚ���
	 *
	 */
	static class MapComparator implements Comparator<Map<String, Object>>{
		
		public int compare(Map<String, Object> o1, Map<String, Object> o2){
			String b1 =  o1.get(SIMILARITY_KEY).toString();
			String b2 =  o2.get(SIMILARITY_KEY).toString();
			
			if(b2 != null){
				return b2.compareTo(b1);
			}
			
			return 0;
		}
		
	}

}
