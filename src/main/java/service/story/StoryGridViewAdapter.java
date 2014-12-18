package service.story;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ai.welcome.R;

import java.util.ArrayList;

import constants.ConstantsCommon;
import model.story.StoryItem;
import tools.AsyncImageViewHelper;

public class StoryGridViewAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<StoryItem> data;
    private int id;
    private Context context;
    private LayoutInflater inflater;


    /**
     * 构造方法
     * @param item
     * @param data
     */
    public StoryGridViewAdapter(int item, Context context,ArrayList<StoryItem> data)
    {
        this.data=data;
//        this.context=mainActivity;
        this.id=item;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2)
    {
        TextView storyTitle=null;
        ImageView storyCover=null;


        if(view==null)
        {
            view=inflater.inflate(id, null);
            storyTitle=(TextView) view.findViewById(R.id.story_tile);
            storyCover=(ImageView) view.findViewById(R.id.story_image);

            //保存view对象到ObjectClass类中
            view.setTag(new ObjectClass(storyTitle,storyCover));
        }
        else
        {
            //得到保存的对象
            ObjectClass objectclass=(ObjectClass) view.getTag();
            storyTitle = objectclass.storyTitle;
            storyCover = objectclass.storyCover;
        }

        StoryItem storyItem= data.get(position);

        // 设置故事名称
        storyTitle.setText(storyItem.getTitle());
        //加载图片资源
        LoadImage(storyCover, storyItem.getStoryCover());
        return view;

    }


    /**
     * 异步加载图片
     * @param img
     * @param path
     */
    private void LoadImage(ImageView img, String path)
    {
        Log.e(ConstantsCommon.LOG_TAG, "故事封面路径:" + path);
        if(path != null && !path.equals(""))
        {
            //异步加载图片资源
            AsyncImageViewHelper async=new AsyncImageViewHelper(img);
            //执行异步加载，并把图片的路径传送过去
            async.execute(path);
        }
    }


    private final class ObjectClass
    {
        TextView storyTitle=null;
        ImageView storyCover=null;

        public ObjectClass(TextView storyTitle, ImageView storyCover)
        {
            this.storyTitle=storyTitle;
            this.storyCover=storyCover;
        }
    }
}
