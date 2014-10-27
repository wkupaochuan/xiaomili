package com.ai.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoryListHandler {
	
	private static String SIMILARITY_KEY = "similarity";
	
	
	
	/**
	 * 根据故事名称，查找最相似的故事列表
	 * @param originalStoryList			故事列表全集
	 * @param needleStoryTitle			目标故事名称
	 * @return
	 */
	public static List<StoryItem> getMostSimilarStorys(List<StoryItem> originalStoryList, String needleStoryTitle ){
		
		List<Map<String, Object>> storyWithSimilarityPoint = new ArrayList<Map<String, Object>>();
		
		List<StoryItem> hintStoryList = new ArrayList<StoryItem>();
		
		
		// 给每个故事计算与目标故事标题的相似度
		for(StoryItem storyItem: originalStoryList){
			Map<String, Object> xx = new HashMap<String, Object>();
			float similarity = getSimilarityOfTwoString(storyItem.getTitle(), needleStoryTitle);
			
			xx.put("story_item", storyItem);
			xx.put(SIMILARITY_KEY, similarity);
			
			storyWithSimilarityPoint.add(xx);
		}
		
		// 排序(根据相似度)
        Collections.sort(storyWithSimilarityPoint, new MapComparator());
        
        // 取出最相似的前3个
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
	 * 计算两个字符串的相似度
	 * @param str1			第一个字符串
	 * @param str2			第二个字符串
	 * 应用场景:DNA分析 　　拼字检查 　　语音辨识 　　抄袭侦测
	 */
	public static float getSimilarityOfTwoString(String str1,String str2) {
		//计算两个字符串的长度。
		int len1 = str1.length();
		int len2 = str2.length();
		//建立上面说的数组，比字符长度大一个空间
		int[][] dif = new int[len1 + 1][len2 + 1];
		//赋初值，步骤B。
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		//计算两个字符是否一样，计算左上的值
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				//取三个值中最小的
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
						dif[i - 1][j] + 1);
			}
		}
		System.out.println("字符串\""+str1+"\"与\""+str2+"\"的比较");
		//取数组右下角的值，同样不同位置代表不同字符串的比较
		System.out.println("差异步骤："+dif[len1][len2]);
		//计算相似度
		float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
		return similarity;
		
	}
	
	
	
	/**
	 * 在所有参数中得到最小值
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
	 * 比较器
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
