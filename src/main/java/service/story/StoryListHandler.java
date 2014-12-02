package service.story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.StoryItem;
import tools.StringHelper;


public class StoryListHandler {
	
	private static String SIMILARITY_KEY = "similarity";

	public static List<StoryItem> getMostSimilarStorys(List<StoryItem> originalStoryList, String needleStoryTitle ){
		
		List<Map<String, Object>> storyWithSimilarityPoint = new ArrayList<Map<String, Object>>();
		
		List<StoryItem> hintStoryList = new ArrayList<StoryItem>();
		

		for(StoryItem storyItem: originalStoryList){
			Map<String, Object> xx = new HashMap<String, Object>();
			float similarity = StringHelper.getSimilarityOfTwoString(storyItem.getTitle(), needleStoryTitle);
			
			xx.put("story_item", storyItem);
			xx.put(SIMILARITY_KEY, similarity);
			
			storyWithSimilarityPoint.add(xx);
		}


        Collections.sort(storyWithSimilarityPoint, new MapComparator());


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
