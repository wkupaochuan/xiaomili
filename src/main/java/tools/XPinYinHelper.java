package tools;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 汉字转化为拼音
 */
public class XPinYinHelper {


    /**
     * 将字符串转化为拼音字符串数组
     * @param str
     * @return
     */
	public static List<String> getPinyin(String str)
	{
		List<String> pinyinRes = new ArrayList<String>();

        // 输出格式设置为没有音调
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

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

		return pinyinRes;
	}
	
}
