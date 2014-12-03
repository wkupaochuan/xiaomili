package model.study;


import java.util.List;

/**
 * 课程model
 */
public class ClassModel {

    // 课程中所有句子
    private List<ClassSentenceModel> sentences;


    public List<ClassSentenceModel> getSentences()
    {
        return this.sentences;
    }

    public void setSentences(List<ClassSentenceModel> sentences)
    {
        this.sentences = sentences;
    }
}
