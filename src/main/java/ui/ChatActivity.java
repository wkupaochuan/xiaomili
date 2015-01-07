package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ai.welcome.R;

import constants.ConstantsCommon;
import service.chat.ConnectManager;

public class ChatActivity extends BaseActivity{

    private TextView tvMsg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        this.tvMsg = (TextView)this.findViewById(R.id.tv_sms);

        ConnectManager.login("182.92.130.192", 5222, "admin", "ijnUHB");
    }


    public void sendMessage(View view)
    {
        String msg = this.tvMsg.getText().toString();

        Log.e(ConstantsCommon.LOG_TAG, "ddd");
        ConnectManager.sendMessage("我在app发消息", "test1@iz255gm1qk6z/Spark 2.6.3");
    }
}
