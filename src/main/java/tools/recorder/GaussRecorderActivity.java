package tools.recorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GaussRecorderActivity extends Activity implements OnClickListener {

	public static final int STOPPED = 0;
	public static final int RECORDING = 1;

	// PcmRecorder recorderInstance = null;
	SpeexRecorder recorderInstance = null;

	Button startButton = null;
	Button stopButton = null;
	Button playButton = null;
	Button exitButon = null;
	TextView textView = null;
	int status = STOPPED;

	String fileName = null;
	SpeexPlayer splayer = null;

	public void onClick(View v) {
        // 开始录音
		if (v == startButton) {
			this.setTitle("start recording");
			fileName = "/mnt/sdcard/1324966898504.spx";
			if (recorderInstance == null) {
				// recorderInstance = new PcmRecorder();
				recorderInstance = new SpeexRecorder(fileName);
				Thread th = new Thread(recorderInstance);
				th.start();
			}
			recorderInstance.setRecording(true);
		}
        // 结束录音
        else if (v == stopButton)
        {
			this.setTitle("stop recording");
			recorderInstance.setRecording(false);
		}
        // 播放键
        else if (v == playButton)
        {
			this.setTitle("playing");
			fileName = "/mnt/sdcard/1324966898504.spx";
			splayer = new SpeexPlayer(fileName);
			splayer.startPlay();

		}
        else if (v == exitButon)
        {
			recorderInstance.setRecording(false);
			System.exit(0);
		}
	}


    /**
     * 创建
     * @param savedInstanceState
     */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		startButton = new Button(this);
		stopButton = new Button(this);
		exitButon = new Button(this);
		playButton = new Button(this);
		textView = new TextView(this);

		startButton.setText("Start");
		stopButton.setText("Stop");
		playButton.setText("play");
		exitButon.setText("exit");
//		textView.setText("android ¼����\n(1)��ȡPCM���." + "\n(2)ʹ��speex����");

		startButton.setOnClickListener(this);
		playButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		exitButon.setOnClickListener(this);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(textView);
		layout.addView(startButton);
		layout.addView(stopButton);
		layout.addView(playButton);
		layout.addView(exitButon);
		this.setContentView(layout);
	}
}