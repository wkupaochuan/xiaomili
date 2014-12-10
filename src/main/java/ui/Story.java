package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ai.welcome.R;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import api.ClientCallBack;
import api.story.GetStoryList;
import constants.ConstantsCommon;
import model.StoryItem;
import tools.BaiduVoice;

public class Story extends BaseActivity{

    // 列表视图
	private GridView gvStoryList;

    // 故事列表
	private List<StoryItem> storyList = new ArrayList<StoryItem>();

    // 百度语音识别对话框
    private BaiduASRDigitalDialog mDialog = null;

    // 百度语音识别回调方法
    private DialogRecognitionListener mRecognitionListener;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_home);

        // 初始化
        this.init();
    }


    /**
     * 初始化
     */
    private void init()
    {
        // 初始化百度语音识别回调
        this.initBiduVoiceRecgnitionListener();

        // 初始化列表视图
        this.gvStoryList = (GridView) this.findViewById(R.id.story_list_grid);

        // 从服务器端获取故事列表
        this.getStoryListFromServer("");
    }


    /**
     * 设定百度语音识别回调
     */
    private void initBiduVoiceRecgnitionListener(){
    	
		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){

				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;
				
				if(rs != null && rs.size() > 0){
                    String recognitionString = rs.get(0);
                    // 记录日志
                    Log.e(ConstantsCommon.LOG_TAG, recognitionString);

                    // 获取新的故事列表
                    Story.this.getStoryListFromServer(recognitionString);
				}
				
			}
            
		};
    }


    /**
     * 更新故事列表
     * @param storyList
     */
    private void updateStoryGridView(List<StoryItem> storyList){
    	this.storyList = storyList;

        ArrayList<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
        for(StoryItem storyItem:this.storyList)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("story_tile", storyItem.getTitle());
            map.put("story_image", R.drawable.story_tab);
            listMap.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this
                , listMap
                , R.layout.story_tab
                , new String[] {"story_tile", "story_image"}
                , new int[]{R.id.story_tile, R.id.story_image}
        );

        this.gvStoryList.setAdapter(simpleAdapter);

        this.setStoryOnclickEvent();
    }

    /**
     * 设置故事列表点击事件
     */
    private void setStoryOnclickEvent(){
    	
    	this.gvStoryList.setOnItemClickListener(new OnItemClickListener(){
    		
    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
    			//playStory((int)id);
			}
    		
    	});
    }


    /**
     * 调用百度语音识别对话框
     * @param view
     */
	public void invokeBaiduVoice(View view){

        if(this.mDialog != null)
        {
            this.mDialog.dismiss();
        }
        this.mDialog = BaiduVoice.initBaiduVoiceDialog(this);

        this.mDialog.setDialogRecognitionListener(this.mRecognitionListener);

		mDialog.show();
	}


    /**
     * 从服务器端请求故事列表
     */
	private void getStoryListFromServer(String searchWords)
	{
		GetStoryList.getStoryList(searchWords, new ClientCallBack() {

            // 请求成功
            public void onSuccess(Object data) {
                List<StoryItem> storyList = (ArrayList<StoryItem>) data;
                if(storyList.size() == 0)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    // 更新故事列表
                    Story.this.updateStoryGridView(storyList);
                }
            }

            // 请求失败
            public void onFailure(String message) {
                Log.e(ConstantsCommon.LOG_TAG, message);
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }

        });
	}
		

}
