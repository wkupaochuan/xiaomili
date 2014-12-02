package service;

import java.util.ArrayList;
import java.util.List;

import model.LearnItemModel;

public class LearnListService {
	
	/**
	 * 获取一首诗
	 * @return
	 */
	public static List<LearnItemModel> getList()
	{
		List<LearnItemModel> list = new ArrayList<LearnItemModel>();
		
		LearnItemModel item = new LearnItemModel();
		
		item.setMediaText("登鹳雀楼王之涣");
		item.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/title.mp3");
		list.add(item);
		
		LearnItemModel item1 = new LearnItemModel();
		item1.setMediaText("白日依山尽");
		item1.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/1.mp3");
		list.add(item1);
		
		LearnItemModel item2 = new LearnItemModel();
		item2.setMediaText("黄河入海流");
		item2.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/2.mp3");
		list.add(item2);
		
		LearnItemModel item3 = new LearnItemModel();
		item3.setMediaText("欲穷千里目");
		item3.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/3.mp3");
		list.add(item3);
		
		LearnItemModel item4 = new LearnItemModel();
		item4.setMediaText("更上一层楼");
		item4.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/4.mp3");
		list.add(item4);
		
		return list;
	}

}
