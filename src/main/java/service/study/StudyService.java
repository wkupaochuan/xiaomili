package service.study;


import model.study.ClassSentenceModel;

import java.util.ArrayList;
import java.util.List;


/**
 * 课程学习service
 */
public class StudyService {

    // 需要学习的所有句子
    private List<ClassSentenceModel> classSentences;

    // 正在学习的句子的序号(-1表示还未开始学习)
    private int learningSentenceNum = -1;

    /**
     * 获取
     */
    public ClassSentenceModel getNextSentence()
    {
        ClassSentenceModel sentence = null;
        if(this.learningSentenceNum < this.classSentences.size())
        {
            this.learningSentenceNum ++;
            sentence = this.classSentences.get(this.learningSentenceNum);
        }
        else{
            //todo
        }
        return sentence;
    }


    /**
     * 获取正在学习的语句
     */
    public ClassSentenceModel getLearningSentence()
    {
        ClassSentenceModel sentence = null;
        if(this.learningSentenceNum >=0 && this.learningSentenceNum < this.classSentences.size())
        {
            sentence = this.classSentences.get(this.learningSentenceNum);
        }
        return sentence;
    }


    /**
     * 构造方法
     */
    public StudyService()
    {
        //
        this.learningSentenceNum = -1;

        // 初始化句子列表 todo
        this.classSentences = new ArrayList<ClassSentenceModel>();


        ClassSentenceModel item = new ClassSentenceModel();
        item.setSentenceId(0);
        item.setMediaText("登鹳雀楼王之涣");
        item.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/title.mp3");
        this.classSentences.add(item);

        ClassSentenceModel item1 = new ClassSentenceModel();
        item1.setSentenceId(1);
        item1.setMediaText("白日依山尽");
        item1.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/1.mp3");
        this.classSentences.add(item1);

        ClassSentenceModel item2 = new ClassSentenceModel();
        item2.setSentenceId(2);
        item2.setMediaText("黄河入海流");
        item2.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/2.mp3");
        this.classSentences.add(item2);

        ClassSentenceModel item3 = new ClassSentenceModel();
        item3.setSentenceId(3);
        item3.setMediaText("欲穷千里目");
        item3.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/3.mp3");
        this.classSentences.add(item3);

        ClassSentenceModel item4 = new ClassSentenceModel();
        item4.setSentenceId(4);
        item4.setMediaText("更上一层楼");
        item4.setMediaUrl("http://toy-admin.wkupaochuan.com/class_files/4.mp3");
        this.classSentences.add(item4);
    }


}
