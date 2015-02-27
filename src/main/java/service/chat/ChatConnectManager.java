package service.chat;

import android.os.Bundle;
import android.util.Log;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import constants.ConstantsCommon;
import ui.ChatActivity;

public class ChatConnectManager {
    public static ConnectionConfiguration connConfig;
    public static XMPPConnection xmppConnection;
    public static ChatManager chatManager;
    public static String UserId;
    public static String User;
    public static Chat chat ;

    public static String serverIP = "182.92.130.192";
    public static int serverPort = 5222;
    public static String password = "ijnUHB";

    private static ChatActivity activity;


    /**
     * 登陆
     * @param Username
     */
    public static void login (final String Username, ChatActivity activity)
    {
        ChatConnectManager.activity = activity;
        connConfig = new ConnectionConfiguration(ChatConnectManager.serverIP, ChatConnectManager.serverPort);

        // 关闭安全模式，必须
        ChatConnectManager.connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        ChatConnectManager.xmppConnection = new XMPPTCPConnection(connConfig);

        new Thread()
        {
            public void run() {
                try {
                    ChatConnectManager.xmppConnection.connect();
                    ChatConnectManager.xmppConnection.login(Username, ChatConnectManager.password);

                    ChatConnectManager.RegisterMessageListener();
                }
                catch (Exception ex) {

                    // todo 完善未注册判别机制
                    if(ex.getMessage().contains("not-authorized"))
                    {
                        ChatConnectManager.register(Username);

                    }

                    Log.e(ConstantsCommon.LOG_TAG, ex.getMessage());
                }
            }
        }.start();
    }


    /**
     * 注册
     * @param userName
     */
    public static void register(final String userName)
    {
        new Thread()
        {
            public void run() {
                try {
                    AccountManager accountManager = AccountManager.getInstance(ChatConnectManager.xmppConnection);
                    accountManager.createAccount(userName, ChatConnectManager.password);

                    // 注册完毕，自动登录
                    ChatConnectManager.login(userName, ChatConnectManager.activity);
                }
                catch (Exception ex) {
                    Log.e(ConstantsCommon.LOG_TAG, ex.getMessage());
                }
            }
        }.start();
    }


    /**
     * 注册消息监听
     */
    protected static void RegisterMessageListener() {

        ChatConnectManager.chatManager = ChatManager.getInstanceFor(ChatConnectManager.xmppConnection);

        ChatConnectManager.chatManager.addChatListener(new ChatManagerListener() {

            public void chatCreated(Chat chat, boolean arg1) {

                chat.addMessageListener(new MessageListener() {


                    /**
                     * 接收到好友发来的消息
                     * @param arg0
                     * @param message
                     */
                    public void processMessage(Chat arg0, Message message) {
//                        ChatConnectManager.sendMessage("我在app", message.getFrom());

                        new NewMsgThread(ChatConnectManager.activity, message.getBody()).start();
                    }

                });

            }
        });

    }


    /**
     * 发送消息(图片、音频类型的消息采取json格式)
     * @param msg
     * @param msgto
     */
    public static void sendMessage(final String msg,final String msgto) {
        ChatConnectManager.chat = chatManager.createChat(msgto, null);

        new Thread()
        {
            public void run() {
                try {
                    ChatConnectManager.chat.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(ConstantsCommon.LOG_TAG, e.getMessage());
                }
            }
        }.start();
    }



    static class NewMsgThread extends Thread{

        ChatActivity activity;//主控制类的引用
        String msgBody;

        public NewMsgThread(ChatActivity activity, String msgBody)//构造方法
        {
            this.activity=activity;
            this.msgBody = msgBody;
        }

        public void run() {
            try {
                Bundle b = new Bundle();
                b.putString("msg_body", this.msgBody);
                android.os.Message msg = new android.os.Message();
                msg.setData(b);
                msg.what = 0;
                this.activity.newMsgHandler.sendMessage(msg);
            } catch (Exception e) {
                Log.e(ConstantsCommon.LOG_TAG, e.getMessage());
            }
        }
    }





}

