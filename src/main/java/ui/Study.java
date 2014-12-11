package ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ai.welcome.R;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import java.util.ArrayList;

import constants.ConstantsCommon;
import model.study.LetterLearnResult;
import model.study.ClassSentenceModel;
import service.study.CheckLearnResult;
import service.study.StudyService;
import tools.BaiduVoice;
import tools.XMediaPlayer;


/**
 * 学习主页
 */

public class Study extends BaseActivity{

    // 百度语音识别窗口
    BaiduASRDigitalDialog mBaiduVoiceDialog;

    // 百度语音识别回调函数
	private DialogRecognitionListener mRecognitionListener;

    // service
    private StudyService studyService = new StudyService();

	LinearLayout sentencesSectionLayout;
	
	/**
	 * 初始化窗口
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_home);

        // 设置百度语音回调
        this.initBiduVoiceRecgnitionListener();

        // 设置播放器播放完毕回调
        setOnMediaPlayerCompletionListener();

        // 初始化layout
        this.sentencesSectionLayout = (LinearLayout) this.findViewById(R.id.text_line);

        // 播放
        this.playNext();
    }
    
    
    /**
     * 调用百度语音识别窗口
     */
	private void invokeBaiduVoiceDialog(){
        if(this.mBaiduVoiceDialog != null)
        {
            this.mBaiduVoiceDialog.dismiss();
        }
        this.mBaiduVoiceDialog = BaiduVoice.initBaiduVoiceDialog(this);
        // 设置回调
        this.mBaiduVoiceDialog.setDialogRecognitionListener(this.mRecognitionListener);
        // 展示窗口
        this.mBaiduVoiceDialog.show();
	}


    /**
     * 设置百度语音识别回调
     */
    private void initBiduVoiceRecgnitionListener(){

		this.mRecognitionListener = new DialogRecognitionListener(){

			public void onResults(Bundle results){

				ArrayList<String> rs = (results !=null)? results.getStringArrayList(RESULTS_RECOGNITION):null;

				if(rs != null && rs.size() > 0){

					for(String str:rs){
						Log.e(ConstantsCommon.LOG_TAG, "百度语音识别结果:" + str.toString());
					}
                    afterRecognition(rs.get(0));
				}

			}

		};
    }


    /**
     * 百度语音识别结束回调
     * @param repeatedSentenceContent
     */
    public void afterRecognition(String repeatedSentenceContent)
    {
    	// 去除识别结果的标点符号
//    	result = result.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
        ClassSentenceModel sentenceModel = this.studyService.getLearningSentence();
        if(sentenceModel != null)
        {
            String originalSentenceContent = sentenceModel.getMediaText();


            // 记录日志
            Log.e(ConstantsCommon.LOG_TAG, "原文:" + originalSentenceContent + "; 识别结果:" + repeatedSentenceContent);

            // 给结果打分
            LetterLearnResult learnResult
                    = CheckLearnResult.gradeRepeatedCentence(originalSentenceContent, repeatedSentenceContent);

            // 根据结果更新字符串
            String learnResultText = learnResult.getResString();
            this.showSentence(learnResultText, sentenceModel.getSentenceId());

            // 记录日志
            Log.e(ConstantsCommon.LOG_TAG, "对比结果:" + learnResultText);

            // 分数太低，重新播放
            if(learnResult.getGrade() < 0.5)
            {
                // todo
                this.replay();
            }
            else{
                // 播放下一首
                this.playNext();
            }
        }
        else{
            //todo
        }
    }


    /**
     * 播放下一句
     */
    public void playNext()
    {
        ClassSentenceModel sentenceModel = this.studyService.getNextSentence();
        if(sentenceModel != null)
        {
            int sentenceId = sentenceModel.getSentenceId();
            String sentenceContent = sentenceModel.getMediaText();
            String sentenceUrl = sentenceModel.getMediaUrl();

            // 显示文字
            this.showSentence(sentenceContent, sentenceId);

            // 播放音频
            XMediaPlayer.play(sentenceUrl);
        }
    }

    /**
     * 重新播放当前句子
     */
    public void replay()
    {
        SoundPool  pool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
        int sourceid = pool.load(this, R.raw.replay_tip, 1);//载入音频流，返回在池中的id
        pool.play(sourceid, 1, 1, 1, 0, 1f);

        ClassSentenceModel sentenceModel = this.studyService.getLearningSentence();
        if(sentenceModel != null)
        {
            int sentenceId = sentenceModel.getSentenceId();
            String sentenceContent = sentenceModel.getMediaText();
            String sentenceUrl = sentenceModel.getMediaUrl();

            // 显示文字
            this.showSentence(sentenceContent, sentenceId);

            // 播放音频
            XMediaPlayer.play(sentenceUrl);
        }
    }


    /**
     * 显示语句
     * @param text
     */
    private void showSentence(String text, int viewId)
    {
        TextView tx = (TextView)this.sentencesSectionLayout.findViewById(viewId);
        if(tx == null)
        {
            tx = new TextView(Study.this);
            tx.setId(viewId);
            tx.setTextSize(30);
            this.sentencesSectionLayout.addView(tx);
        }
        else{

        }
    	tx.setText(Html.fromHtml(text));
    }


	/**
	 * 设置单个音频播放完毕事件
	 */
	private void setOnMediaPlayerCompletionListener(){
		XMediaPlayer.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer arg0) {
                invokeBaiduVoiceDialog();
            }
        });
	}

    
}
