package ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.ai.welcome.R;

import java.util.ArrayList;

import constants.ConstantsCommon;
import model.story.StoryItem;

public class Story extends BaseActivity implements StoryListFragment.StoryItemOnClickListener, PlayingStoryFragment.ClosePlayingStoryOnClickListener{

    // 故事列表
    ArrayList<StoryItem> storyItemsList;

    private StoryListFragment storyListFragment;

    private PlayingStoryFragment playingStoryFragment;

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
        // 设置默认视图
        this.setDefaultFragment();

        //
        this.storyItemsList = this.storyListFragment.storyList;
    }

    /**
     * 设置默认视图
     */
    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        this.storyListFragment = new StoryListFragment();
        transaction.replace(R.id.id_content, this.storyListFragment);
        transaction.commit();
    }


    /**
     * 故事列表点击事件
     * @param position
     */
    public void onItemClick(int position){
        Log.e(ConstantsCommon.LOG_TAG, "被点击:" + position);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        this.playingStoryFragment = new PlayingStoryFragment();
        transaction.replace(R.id.id_content, this.playingStoryFragment);
        transaction.commit();
        this.playingStoryFragment.updateStoryList(this.storyListFragment.storyList, position);
    }


    /**
     * 关闭正在播放的故事页面
     */
    public void ClosePlayingStoryOnClick()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        this.storyListFragment = new StoryListFragment();
        transaction.replace(R.id.id_content, this.storyListFragment);
        transaction.commit();
    }


}
