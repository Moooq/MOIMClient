package com.jammy.mchsclient.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jammy.mchsclient.activity.ChatActivity;
import com.jammy.mchsclient.fragment.ChatFragment;
import com.jammy.mchsclient.fragment.ContactsFragment;
import com.jammy.mchsclient.model.Msg;

import static com.jammy.mchsclient.MyApplication.activityMap;

public class ClientListenThread extends Thread {
    public static final String TAG = "ClientListenThread";
    private Socket socket = null;
    private boolean isStart = true;
    private ObjectInputStream ois = null;
    private Handler handler;

    public ClientListenThread(Socket socket) {
        // TODO Auto-generated constructor stub
        this.socket = socket;
    }

    public ClientListenThread(Socket socket, Handler handler) {
        // TODO Auto-generated constructor stub
        this.socket = socket;
        this.handler = handler;
    }

    public void close() {
        isStart = false;
    }

    public void run() {
        while (isStart) {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                Object obj = ois.readObject();
                if (obj instanceof Msg) {
                    Msg message = (Msg) obj;
                    Log.i(TAG, "getMessage:"+message.getMessagecontent());
                    if (message.getMessagetype() == Msg.STRING) {
                        Message mesg = new Message();
                        mesg.what = 0;
                        mesg.obj = message;
                        if (activityMap.containsKey("ChatActivity")) {
                            ChatActivity.handler.sendMessage(mesg);
                        }
                        Message mesg2 = new Message();
                        mesg2.what = 0;
                        mesg2.obj = message;
                        ChatFragment.handler.sendMessage(mesg2);

                    }
                    if(message.getMessagetype() == Msg.APPLY){
                        Message mesg = new Message();
                        mesg.what = 0;
                        mesg.obj = message;
                        ContactsFragment.handler.sendMessage(mesg);
                    }
                }

            } catch (StreamCorruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                close();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                close();
            }
        }
    }
}
