package ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ai.welcome.R;

import java.util.ArrayList;

import api.ClientCallBack;
import api.study.GetClassesForToday;
import constants.ConstantsCommon;
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

        this.getClassesFromServer();
    }


    /**
     * 更新课程列表listview
     * @param classes
     */
    private void updateClassListView(ArrayList<ClassModel> classes)
    {
        StudyHomeClassListViewAdapter adapter = new StudyHomeClassListViewAdapter(R.layout.study_home_class_tab_test, this, classes);

        this.classListViewForToday.setAdapter(adapter);
    }


    /**
     * 从服务端获取今日课程列表
     */
    private void getClassesFromServer()
    {
        GetClassesForToday.getClass(new ClientCallBack() {
            @Override
            public void onSuccess(Object data) {
                ArrayList<ClassModel> classes = (ArrayList<ClassModel>)data;
                if (classes.size() == 0) {
                    Toast toast = Toast.makeText(StudyHomeTest.this, "暂无数据", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // 更新课程列表
                    StudyHomeTest.this.updateClassListView(classes);
                }
            }

            @Override
            public void onFailure(String message) {

                Log.e(ConstantsCommon.LOG_TAG, message);
                Toast toast = Toast.makeText(StudyHomeTest.this, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    /**
     * 返回主页
     * @param view
     */
    public void backToPre(View view)
    {
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }
    

}
