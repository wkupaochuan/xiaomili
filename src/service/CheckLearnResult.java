package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tools.XPinYinHelper;

import model.LetterLearnResult;

public class CheckLearnResult {
	
	private static final float minGrade = (float) 0.5;
	
	
	/**
	 * 给跟读结果打分
	 * @param standard				标准语句
	 * @param repeatedCentence		跟读语句
	 */
	public static LetterLearnResult gradeRepeatedCentence(String standard, String repeatedCentence)
	{
		int missingLetterCount = 0;
		Map<String, Object> compareResult = new HashMap<String, Object>();
		String resString = "";
		
		for(int i = 0; i < standard.length(); ++i)
		{
			if(i > repeatedCentence.length() || standard.charAt(i) != repeatedCentence.charAt(i))
			{
				missingLetterCount ++;
				compareResult.put(standard.charAt(i) + "", false);
				resString += "<font color=red>" + standard.charAt(i) + "</font>";
			}
			else{
				compareResult.put(standard.charAt(i) + "", true);
				resString += "<font>" + standard.charAt(i) + "</font>";
			}
		}
		
		float grade = missingLetterCount/standard.length();
		
		if( grade > 0 && grade < CheckLearnResult.minGrade)
		{
			return CheckLearnResult.gradeRepeatedCentenceByPinyin(standard, repeatedCentence);
		}
		LetterLearnResult res = new LetterLearnResult();
		res.setGrade(grade);
		res.setLetterCompareResult(compareResult);
		res.setResString(resString);
		return res;
	}
	
	
	
	/**
	 * 获取识别后的字符串
	 * @param letterResult
	 * @return
	 */
	public static String getLearnResultString(Map<String, Object> letterResult)
	{
		String res = "";
		Iterator<String> iter = letterResult.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			boolean result =  (Boolean) letterResult.get(key);
			
			if(result)
			{
				res += "<font color=red>" + key + "</font>";
			}
			else{
				res += "<font >" + key + "</font>";
			}
		}
		
		return res;
	}
	
	
	/**
	 * 根据拼音打分
	 * @param standard
	 * @param repeatedCentence
	 * @return
	 */
	private static LetterLearnResult gradeRepeatedCentenceByPinyin(String standard, String repeatedCentence)
	{
		List<String> standardPinyin =  XPinYinHelper.getPinyin(standard);
		List<String> repeatedCentencedPinyin =  XPinYinHelper.getPinyin(repeatedCentence);
		String resString = "";
		
		int missingLetterCount = 0;
		Map<String, Object> compareResult = new HashMap<String, Object>();
		
		for(int i = 0; i < standardPinyin.size(); ++i)
		{
			if(i > repeatedCentencedPinyin.size() || standardPinyin.get(i) != repeatedCentencedPinyin.get(i))
			{
				missingLetterCount ++;
				compareResult.put(standardPinyin.get(i), false);
				resString += "<font color=red>" + standard.charAt(i) + "</font>";
			}
			else{
				compareResult.put(standardPinyin.get(i), true);
				resString += "<font color>" + standard.charAt(i) + "</font>";
			}
		}
		
		float grade = missingLetterCount/standard.length();
		
		LetterLearnResult res = new LetterLearnResult();
		res.setGrade(grade);
		res.setLetterCompareResult(compareResult);
		res.setResString(resString);
		return res;
	}
	
}
