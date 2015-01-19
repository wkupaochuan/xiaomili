package service.chat;

import android.app.Activity;
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

public class ConnectManager {
    public static ConnectionConfiguration connConfig;
    public static XMPPConnection xmppConnection;
    public static ChatManager chatManager;
    public static Activity activity;
    public static String UserId;
    public static String User;
    public static Chat chat ;

    public static String serverIP = "182.92.130.192";
    public static int serverPort = 5222;
    public static String password = "ijnUHB";


    /**
     * 登陆
     * @param Username
     */
    public static void login (final String Username)
    {
        connConfig = new ConnectionConfiguration(ConnectManager.serverIP, ConnectManager.serverPort);

        // 关闭安全模式，必须
        ConnectManager.connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        ConnectManager.xmppConnection = new XMPPTCPConnection(connConfig);

        new Thread()
        {
            public void run() {
                try {
                    ConnectManager.xmppConnection.connect();
                    ConnectManager.xmppConnection.login(Username, ConnectManager.password);

                    ConnectManager.RegisterMessageListener();
                }
                catch (Exception ex) {

                    // todo 完善未注册判别机制
                    if(ex.getMessage().contains("not-authorized"))
                    {
                        ConnectManager.register(Username);

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
                    AccountManager accountManager = AccountManager.getInstance(ConnectManager.xmppConnection);
                    accountManager.createAccount(userName, ConnectManager.password);

                    // 注册完毕，自动登录
                    ConnectManager.login(userName);
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

        ConnectManager.chatManager = ChatManager.getInstanceFor(ConnectManager.xmppConnection);

        ConnectManager.chatManager.addChatListener(new ChatManagerListener() {

            public void chatCreated(Chat chat, boolean arg1) {
                chat.addMessageListener(new MessageListener() {

                    public void processMessage(Chat arg0, Message message) {
                        ConnectManager.sendMessage("我在app", message.getFrom());
                    }
                });

            }
        });

    }


    /**
     * 发送文字消息
     * @param msg
     * @param msgto
     */
    public static void sendMessage(final String msg,final String msgto) {
        ConnectManager.chat = chatManager.createChat(msgto, null);

        new Thread()
        {
            public void run() {
                try {
                    ConnectManager.chat.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(ConstantsCommon.LOG_TAG, e.getMessage());
                }
            }
        }.start();
    }



}

