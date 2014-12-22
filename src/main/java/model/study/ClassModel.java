package model.study;


import java.util.List;

/**
 * 课程model
 */
public class ClassModel {

    // 课程名称
    private String classTitle;

    // 课程封面
    private String classCover;

    // 课程学习进度
    private int rateOfProgress;

    // 课程得分
    private int gradeForClass;

    // 课程中所有句子
    private List<ClassSentenceModel> sentences;





    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassCover() {
        return classCover;
    }

    public void setClassCover(String classCover) {
        this.classCover = classCover;
    }

    public int getRateOfProgress() {
        return rateOfProgress;
    }

    public void setRateOfProgress(int rateOfProgress) {
        this.rateOfProgress = rateOfProgress;
    }

    public int getGradeForClass() {
        return gradeForClass;
    }

    public void setGradeForClass(int gradeForClass) {
        this.gradeForClass = gradeForClass;
    }

    public List<ClassSentenceModel> getSentences()
    {
        return this.sentences;
    }

    public void setSentences(List<ClassSentenceModel> sentences)
    {
        this.sentences = sentences;
    }
}
