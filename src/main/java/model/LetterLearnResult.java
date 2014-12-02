
package model;


import java.util.Map;

public class LetterLearnResult {
	
	private float grade;
	private Map<String, Object> letterCompareResult;
	private String resString;
	public String getResString() {
		return resString;
	}
	public void setResString(String resString) {
		this.resString = resString;
	}
	public float getGrade() {
		return grade;
	}
	public void setGrade(float grade) {
		this.grade = grade;
	}
	public Map<String, Object> getLetterCompareResult() {
		return letterCompareResult;
	}
	public void setLetterCompareResult(Map<String, Object> letterCompareResult) {
		this.letterCompareResult = letterCompareResult;
	}
	
	
	
}
