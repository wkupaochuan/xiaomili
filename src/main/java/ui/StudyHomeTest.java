package ui;

import android.os.Bundle;
import android.widget.ListView;

import com.ai.welcome.R;

import java.util.ArrayList;

import model.study.ClassModel;
import service.study.StudyHomeClassListViewAdapter;


/**
 * 学习主页
 */

public class StudyHomeTest extends BaseActivity{

    // 今日课程列表
    private ListView classListViewForToday;

	
	/**
	 * 初始化窗口
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_home_test);

        // 初始化
        this.init();
    }


    /**
     * 初始化
     */
    private void init()
    {
        // 初始化今日课程列表
        this.classListViewForToday = (ListView)this.findViewById(R.id.class_list_for_today);

        StudyHomeClassListViewAdapter adapter = new StudyHomeClassListViewAdapter(R.layout.study_home_class_tab_test, this, this.getClassList());

        this.classListViewForToday.setAdapter(adapter);
    }



    private ArrayList<ClassModel> getClassList()
    {
        ArrayList<ClassModel> classList = new ArrayList<ClassModel>();
        for(int i = 0; i < 3; ++i)
        {
            ClassModel tmp = new ClassModel();
            tmp.setClassTitle("测试");

            tmp.setClassCover("http://toy-admin.wkupaochuan.com/mp3_files/62c7c2a287015e5f5a59d1b1d701a52f.jpg");

            tmp.setRateOfProgress(80);

            tmp.setGradeForClass(80);

            classList.add(tmp);
        }


        return classList;
    }

    

}
