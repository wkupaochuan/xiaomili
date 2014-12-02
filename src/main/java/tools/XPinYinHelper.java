package tools;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 将中文字符串转化为拼音字符串数组
 * @author xiaomili
 */
public class XPinYinHelper {

	public static List<String> getPinyin(String str)
	{
		// 返回的拼音字符串数组
		List<String> pinyinRes = new ArrayList<String>();
		
		// 拼音输出格式，没有音调
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		
		// 转化为拼音数组
		try {
			char[] charArray = str.toCharArray();
			for(char tmp : charArray)
			{
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(tmp, format);
				if(pinyin != null)
				{
					pinyinRes.add(pinyin[0]);	
				}
				else{
					pinyinRes.add(tmp + "");	
				}
				
				
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			
		}
		
		// 返回
		return pinyinRes;
	}
	
}
