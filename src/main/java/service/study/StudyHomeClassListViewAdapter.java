package service.study;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ai.welcome.R;

import java.util.ArrayList;

import constants.ConstantsCommon;
import model.study.ClassModel;
import tools.AsyncImageViewHelper;

/**
 * 学习主页，课程列表adapter
 */
public class StudyHomeClassListViewAdapter extends BaseAdapter implements ListAdapter{


    private ArrayList<ClassModel> classList;
    private int id;
    private LayoutInflater inflater;
    private Context context;

    /**
     * 构造方法
     * @param item
     * @param context
     * @param classList
     */
    public StudyHomeClassListViewAdapter(int item, Context context,ArrayList<ClassModel> classList)
    {
        this.classList = classList;
        this.id = item;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }



    public int getCount()
    {
        return classList.size();
    }


    public Object getItem(int position)
    {
        return classList.get(position);
    }


    public long getItemId(int position)
    {
        return position;
    }


    public View getView(int position, View view, ViewGroup arg2)
    {
        TextView classTitle;
        ImageView classCover;
        ProgressBar progressBar;
        LinearLayout llGrade;


        if(view == null)
        {
            view = inflater.inflate(id, null);
            classTitle = (TextView) view.findViewById(R.id.class_title);
            classCover = (ImageView) view.findViewById(R.id.class_cover);
            progressBar = (ProgressBar) view.findViewById(R.id.class_progress);
            llGrade = (LinearLayout) view.findViewById(R.id.ll_class_grade);

            //保存view对象到ObjectClass类中
            view.setTag(new ObjectClass(classTitle, classCover, progressBar, llGrade));
        }
        else
        {
            //得到保存的对象
            ObjectClass objectclass=(ObjectClass) view.getTag();
            classTitle = objectclass.classTitle;
            classCover = objectclass.classCover;
            progressBar = objectclass.rateOfProgress;
            llGrade = objectclass.llGrade;
        }

        ClassModel oClass = this.classList.get(position);

        // 设置课程名称
        classTitle.setText(oClass.getClassTitle());

        // 设置课程封面
        this.LoadImage(classCover, oClass.getClassCover());

        // 设置课程进度
        progressBar.setProgress(oClass.getRateOfProgress());

        // 设置课程分数
        this.setClassGrade(llGrade, oClass.getGradeForClass());

        return view;
    }

    /**
     * 设置分数显示
     * @param llGrade
     * @param grade
     */
    private void setClassGrade(LinearLayout llGrade, int grade)
    {
        llGrade.removeAllViews();
        int starCount;
        if(grade < 60)
        {
            starCount = 1;
        }
        else{
            starCount = (grade - 60)/10 + 2;
        }

        for(int i = 0; i < starCount; i++)
        {
            ImageView imageView = new ImageView(this.context);

            imageView.setBackgroundResource(R.drawable.class_grade_star);

            llGrade.addView(imageView);
        }
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
        // 课程名称
        TextView classTitle;

        // 课程封面
        ImageView classCover;

        // 课程学习进度
        ProgressBar rateOfProgress;

        // 课程分数
        LinearLayout llGrade;


        public ObjectClass(TextView classTitle, ImageView classCover, ProgressBar rateOfProgress, LinearLayout llGrade)
        {
            this.classTitle = classTitle;
            this.classCover = classCover;
            this.rateOfProgress = rateOfProgress;
            this.llGrade = llGrade;
        }
    }
}
