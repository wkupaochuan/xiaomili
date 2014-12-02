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
		int foundedLetterCount = 0;
		Map<String, Object> compareResult = new HashMap<String, Object>();
		String resString = "";
		boolean foundFlag = false;
		
		for(int i = 0; i < standard.length(); ++i)
		{
			foundFlag = false;
			for(int j = 0; j < repeatedCentence.length(); ++j)
			{
				if(standard.charAt(i) == repeatedCentence.charAt(j))
				{
					foundFlag = true;
				}
			}
			
			if(foundFlag)
			{
				foundedLetterCount ++;
				compareResult.put(standard.charAt(i) + "", false);
				resString += "<font>" + standard.charAt(i) + "</font>";
			}
			else{
				compareResult.put(standard.charAt(i) + "", true);
				resString += "<font color=red>" + standard.charAt(i) + "</font>";
			}
		}
		
		float grade = (float)foundedLetterCount/standard.length();
		
		if(grade < CheckLearnResult.minGrade)
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
		
		int foundedLetterCount = 0;
		Map<String, Object> compareResult = new HashMap<String, Object>();
		
		boolean foundFlag = false;
		
		for(int i = 0; i < standardPinyin.size(); ++i)
		{
			foundFlag = false;
			for(int j = 0; j < repeatedCentencedPinyin.size(); ++j)
			{
				if(standardPinyin.get(i).equalsIgnoreCase(repeatedCentencedPinyin.get(j)))
				{
					foundFlag = true;
				}
			}
			
			if(foundFlag)
			{
				foundedLetterCount ++;
				compareResult.put(standard.charAt(i) + "", false);
				resString += "<font>" + standard.charAt(i) + "</font>";
			}
			else{
				compareResult.put(standard.charAt(i) + "", true);
				resString += "<font color=red>" + standard.charAt(i) + "</font>";
			}
		}
		
		float grade = foundedLetterCount/standard.length();
		
		LetterLearnResult res = new LetterLearnResult();
		res.setGrade(grade);
		res.setLetterCompareResult(compareResult);
		res.setResString(resString);
		return res;
	}
	
}
