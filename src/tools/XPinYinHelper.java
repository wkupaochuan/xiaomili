package tools;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * �������ַ���ת��Ϊƴ���ַ�������
 * @author xiaomili
 */
public class XPinYinHelper {

	public static List<String> getPinyin(String str)
	{
		// ���ص�ƴ���ַ�������
		List<String> pinyinRes = new ArrayList<String>();
		
		// ƴ�������ʽ��û������
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		
		// ת��Ϊƴ������
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
		
		// ����
		return pinyinRes;
	}
	
}
