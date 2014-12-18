package ui;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.welcome.R;

import java.util.ArrayList;

import constants.ConstantsCommon;
import model.story.StoryItem;
import tools.AsyncImageViewHelper;
import tools.XMediaPlayer;

public class PlayingStoryFragment extends BaseFragment{

    // 视图
    View view;

    // 故事列表
    private ArrayList<StoryItem> storyItemsList;

    // 正在播放的故事编号(-1表示初始位置，没有在播放的故事)
    private int activeItemNum = -1;

    // 播放状态(-1:初始状态, 0:停止状态, 1:播放状态, 2:暂停状态)
    private int playIngStatus = -1;

    private ClosePlayingStoryOnClickListener closeOnClickListener;


    // 播放控制按钮
    private ImageButton btnFoward;

    private ImageButton btnNext;

    private ImageButton btnPlayOrPause;

    // 关闭按钮
    private ImageView imageViewClose;

    // 故事封面
    private ImageView imageViewStoryCover;

    // 标题
    private TextView tvStoryTitle;

    /**
     * 页面被创建时触发
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.playing_story, container, false);

        // 初始化
        this.init();
        return this.view;
    }

    public void onStart()
    {
        super.onStart();
        this.play(this.activeItemNum);
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            this.closeOnClickListener = (ClosePlayingStoryOnClickListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");
        }
    }

    // 初始化方法
    private void init()
    {
        // 初始化按钮
        this.btnFoward = (ImageButton) this.view.findViewById(R.id.btn_foward_story);
        this.btnFoward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayingStoryFragment.this.btnFowardOnClick();
            }
        });
        this.btnNext = (ImageButton) this.view.findViewById(R.id.btn_next_story);
        this.btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PlayingStoryFragment.this.btnNextOnClick();
            }
        });
        this.btnPlayOrPause = (ImageButton) this.view.findViewById(R.id.btn_play_or_pause);
        this.btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PlayingStoryFragment.this.btnPlayOrPauseOnClick();
            }
        });

        this.imageViewClose = (ImageView)this.view.findViewById(R.id.btn_close);
        this.imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOnClickListener.ClosePlayingStoryOnClick();
            }
        });

        // 初始化故事封面ImageView
        this.imageViewStoryCover = (ImageView) this.view.findViewById(R.id.image_view_story_cover);

        // 初始化
        this.tvStoryTitle = (TextView) this.view.findViewById(R.id.text_view_story_title);
    }


    /**
     * 下一首按钮点击事件
     */
    private void btnNextOnClick()
    {
        this.activeItemNum ++;
        if(this.activeItemNum >= this.storyItemsList.size())
        {
            this.activeItemNum = 0;
        }
        this.play(this.activeItemNum);
    }


    /**
     * 上一首按钮点击事件
     */
    private void btnFowardOnClick()
    {
        this.activeItemNum --;
        if(this.activeItemNum < 0)
        {
            this.activeItemNum = this.storyItemsList.size() - 1;
        }
        this.play(this.activeItemNum);
    }




    /**
     * 播放暂停按钮点击事件
     */
    private void btnPlayOrPauseOnClick()
    {
        if(!XMediaPlayer.getInstance().isPlaying())
        {
            this.play(this.activeItemNum);
        }
        else{
            this.pause();
        }
    }


    /**
     * 更新故事列表，和当前故事
     * @param storyItemsList
     * @param position
     */
    public void updateStoryList(ArrayList<StoryItem> storyItemsList, int position)
    {
        this.storyItemsList = storyItemsList;
        this.activeItemNum = position;
    }


    /**
     * 播放
     */
    public void play(int position)
    {
        // 设置播放按钮
        this.btnPlayOrPause.setBackgroundResource(R.drawable.pause_selecor);
        Log.e(ConstantsCommon.LOG_TAG, "播放故事编号:" + position);
        if(position >= 0 && position < this.storyItemsList.size())
        {
            StoryItem storyItem = this.storyItemsList.get(position);
            this.playIngStatus = 1;
            this.activeItemNum = position;
            String storyPath = storyItem.getLocation();
            storyPath = "http://toy-admin.wkupaochuan.com" + storyPath;

            // 设置标题
            String storyTile = storyItem.getTitle();
            this.tvStoryTitle.setText(storyTile);

            // todo 设置故事封面 toto
            String storyCoverPath = storyItem.getStoryCover();
            if(storyCoverPath != null && !storyCoverPath.equals(""))
            {
                //异步加载图片资源
                AsyncImageViewHelper async = new AsyncImageViewHelper(this.imageViewStoryCover);
                //执行异步加载，并把图片的路径传送过去
                async.execute(storyCoverPath);
            }

            // 播放故事
            Log.e(ConstantsCommon.LOG_TAG, "播放故事地址:" + storyPath);
            XMediaPlayer.play(storyPath);
        }
        else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "要播放的故事不存在", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * 暂停
     */
    public void pause()
    {
        this.btnPlayOrPause.setBackgroundResource(R.drawable.play_selecor);
        XMediaPlayer.pause();
    }


    public interface ClosePlayingStoryOnClickListener{
        public void ClosePlayingStoryOnClick();
    }



}
