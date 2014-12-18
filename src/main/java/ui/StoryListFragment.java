package ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ai.welcome.R;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import java.util.ArrayList;

import api.ClientCallBack;
import api.story.GetStoryList;
import constants.ConstantsCommon;
import model.story.StoryItem;
import service.story.StoryGridViewAdapter;
import tools.BaiduVoice;

public class StoryListFragment extends BaseFragment{

    // 列表视图
	private GridView gvStoryList;

    // 故事列表
	public ArrayList<StoryItem> storyList = new ArrayList<StoryItem>();

    // 百度语音识别对话框
    private BaiduASRDigitalDialog mDialog = null;

    // 百度语音识别回调方法
    private DialogRecognitionListener mRecognitionListener;

    private View view;

    StoryItemOnClickListener mListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.story_list, container, false);

        this.init();
        return this.view;
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener =(StoryItemOnClickListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");
        }
    }


    /**
     * 初始化
     */
    private void init()
    {
        // 初始化百度语音识别回调
        this.initBiduVoiceRecgnitionListener();

        // 初始化列表视图
        this.gvStoryList = (GridView) this.view.findViewById(R.id.story_list_grid);

        // 从服务器端获取故事列表
        this.getStoryListFromServer("");

        ImageButton button = (ImageButton)this.view.findViewById(R.id.btn_baidu_voice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryListFragment.this.invokeBaiduVoice();
            }
        });

        // 设置返回主页点击事件
        ImageView backImageView = (ImageView)this.view.findViewById(R.id.backImage);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToWelcomePageOnClick();
            }
        });
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
                    StoryListFragment.this.getStoryListFromServer(recognitionString);
				}
			}
            
		};
    }


    /**
     * 更新故事列表
     * @param storyList
     */
    private void updateStoryGridView(ArrayList<StoryItem> storyList){
    	this.storyList = storyList;

        StoryGridViewAdapter adapter = new StoryGridViewAdapter(R.layout.story_tab, getActivity(), storyList);

        this.gvStoryList.setAdapter(adapter);

        this.setStoryOnclickEvent();
    }


    /**
     * 设置故事列表点击事件
     */
    private void setStoryOnclickEvent(){
    	
    	this.gvStoryList.setOnItemClickListener(
                new OnItemClickListener(){

    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                mListener.onItemClick(position);
			}

    	});
    }

    public interface StoryItemOnClickListener{
        public void onItemClick(int position);

        // 返回主页
        public void backToWelcomePageOnClick();
    }


    /**
     * 调用百度语音识别对话框
     */
	public void invokeBaiduVoice(){

        if(this.mDialog != null)
        {
            this.mDialog.dismiss();
        }
        this.mDialog = BaiduVoice.initBaiduVoiceDialog(getActivity());

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
                ArrayList<StoryItem> storyList = (ArrayList<StoryItem>) data;
                if(storyList.size() == 0)
                {
                    Toast toast = Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    // 更新故事列表
                    StoryListFragment.this.updateStoryGridView(storyList);
                }
            }

            // 请求失败
            public void onFailure(String message) {
                Log.e(ConstantsCommon.LOG_TAG, message);
                Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
                toast.show();
            }

        });
	}
		

}
