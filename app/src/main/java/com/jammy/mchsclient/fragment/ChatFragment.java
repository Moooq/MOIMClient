package com.jammy.mchsclient.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.ChatActivity;
import com.jammy.mchsclient.db.DBManager;
import com.jammy.mchsclient.model.Msg;
import com.jammy.mchsclient.model.ReturnUnRead;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.jammy.mchsclient.MyApplication.activityMap;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class ChatFragment extends Fragment {
    int CHATLOG = 0;
    ListView lvChat;
    DBManager dbManager = new DBManager();
    List<Msg> chatList = new ArrayList<Msg>();
    List<Msg> msgList = new ArrayList<Msg>();
    public static Handler handler = null;

    private MyBaseAdapter adapter;
    ReturnUnRead returnUnRead;
    int getunread = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ChatView = inflater.inflate(R.layout.fragment_chat, container, false);
        lvChat = (ListView) ChatView.findViewById(R.id.lv_chat);
        init();
        return ChatView;
    }

    void init() {
        getDBDatas();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Msg newMsg = (Msg) msg.obj;
                int newN = 0;
                dbManager.insertData(newMsg, getContext());
                if (chatList != null) {
                    for (int j = 0; j < chatList.size(); j++) {
                        if (chatList.get(j).getFriend().equals(newMsg.getFriend())) {
                            newN = chatList.get(j).getType();
                            if (newN < 0) {
                                newN = 0;
                            }
                            chatList.remove(j);
                        }
                    }

                    newMsg.setType(newN + 1);
                }
                if(activityMap.containsKey("ChatActivity")){
                    Log.i(TAG, "handleMessage: 1");
                    newMsg.setType(0);
                }
                chatList.add(newMsg);
                List<Msg> unreadList = new ArrayList<Msg>();
                unreadList = chatList;
                getunread = unreadList.size();
                if (chatList.size()==1){
                    adapter = new MyBaseAdapter(listToMsgs(unreadList));
                    lvChat.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        };
    }


    private void getDBDatas() {
        msgList = dbManager.queryDatas(getContext());
        if (msgList.size() > 0) {
            List<Msg> unreadList = new ArrayList<Msg>();
            unreadList = getUnreadList(listToMsgs(msgList));
            getunread = unreadList.size();
            Log.i("dbdbdb", "getunread: " + getunread);
            adapter = new MyBaseAdapter(listToMsgs(unreadList));
            lvChat.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "NO MESSAGE", Toast.LENGTH_SHORT).show();
        }
        getNetDatas();
    }

    private Msg[] listToMsgs(List<Msg> mss) {
        Msg[] m1 = new Msg[mss.size()];
        for (int i = 0; i < mss.size(); i++) {
            m1[i] = mss.get(i);
        }
        return m1;
    }

    private List<Msg> MsgsToList(Msg[] mss) {
        List<Msg> m1 = new ArrayList<Msg>();
        for (int i = 0; i < mss.length; i++) {
            m1.add(mss[i]);
        }
        return m1;
    }

    private List<Msg> getUnreadList(Msg[] ms) {
        List<Msg> mes = new ArrayList<Msg>();
        Log.i("123", "length:" + ms.length);
        if (ms.length > 1) {
            for (int i = 0; i < ms.length - 1; i++) {
                if (!ms[i].getFriend().equals(ms[i + 1].getFriend())) {
                    mes.add(ms[i]);
                }
            }
        }
        mes.add(ms[ms.length - 1]);
        chatList = mes;
        return mes;
    }

    private void getNetDatas() {
        OkGo.post(API.RECEIVE_UNREAD_API)
                .params("userInfo.username", MyApplication.userOnLine.getUsername())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        returnUnRead = gson.fromJson(s, ReturnUnRead.class);
                        if (returnUnRead.getCode() == 200 && returnUnRead.getMsg().equals("SUCCESS") && returnUnRead.getData().length > 0) {
                            dbManager.insertDatas(returnUnRead.getData(), getContext());
                            Log.i("type", "onSuccess: " + returnUnRead.getData().length + ":" + MsgsToList(returnUnRead.getData()).size());
                            new GetUnReadMessages().execute(MsgsToList(returnUnRead.getData()));
                        } else {
                        }
                    }
                });
    }

    private class MyBaseAdapter extends BaseAdapter {
        private Msg[] data;

        private MyBaseAdapter(Msg[] list) {
            this.data = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return getunread;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return data[getunread - 1 - arg0];
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            final View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_chat, null);
            final TextView tvChaterName = (TextView) view.findViewById(R.id.tv_chater_name);
            final TextView tvChatMsg = (TextView) view.findViewById(R.id.tv_chat_msg);
            final TextView tvChatTime = (TextView) view.findViewById(R.id.tv_chat_time);
            final LinearLayout layoutItemChat = (LinearLayout)view.findViewById(R.id.layout_item_chat);
            layoutItemChat.getBackground().setAlpha(230);
            final TextView tvUnreadNum = (TextView) view.findViewById(R.id.tv_unread_num);
            tvChaterName.setText(data[getunread - 1 - arg0].getFriend());
            tvChatMsg.setText(data[getunread - 1 - arg0].getMessagecontent() + "");
            tvChatTime.setText(data[getunread - 1 - arg0].getTime());
            Log.i("123", "getView: " + data[getunread - 1 - arg0].getType() + "");
            if (data[getunread - 1 - arg0].getType() <= 0) {
                tvUnreadNum.setVisibility(View.GONE);
            } else {
                tvUnreadNum.setVisibility(View.VISIBLE);
                tvUnreadNum.setText(data[getunread - 1 - arg0].getType() + "");
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chat", data[getunread - 1 - arg0].getFriend());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHATLOG);
                }
            });
            return view;
        }
    }

    class GetUnReadMessages extends AsyncTask<List<Msg>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(List<Msg>... params) {
            Log.i("asynctask", "start");

            Msg un = new Msg();
            Log.i("type", "" + params);
            if (params[0].size() == 1) {
                un = params[0].get(0);
                Log.i("chatlist", "un:" + un.getFriend() + ":" + un.getMessagecontent());
                int n2 = 0;
                for (int j = 0; j < chatList.size(); j++) {
                    if (un.getFriend().equals(chatList.get(j).getFriend())) {
                        chatList.remove(j);
                    }
                }
                un.setType(1);
                chatList.add(un);
            } else {
                for (int i = 0; i < params[0].size() - 1; i++) {
                    int n = 0;
                    un = params[0].get(i);
                    for (int j = 0; j < chatList.size(); j++) {
                        if (params[0].get(i).getFriend().equals(chatList.get(j).getFriend())) {
                            n = chatList.get(j).getType();
                            if (n < 0) {
                                n = 0;
                            }
                            chatList.remove(j);
                        }
                    }
                    Log.i("type", "before:" + un.getType());
                    un.setType(n + 1);
                    Log.i("type", "after:" + un.getType());
                    chatList.add(un);
                }
                Log.i("type", "doInBackground: ");
                un = params[0].get(params[0].size() - 1);
                Log.i("chatlist", "un:" + un.getFriend() + ":" + un.getMessagecontent());
                int n2 = 0;
                for (int j = 0; j < chatList.size(); j++) {
                    if (un.getFriend().equals(chatList.get(j).getFriend())) {
                        n2 = chatList.get(j).getType();
                        if (n2 < 0) {
                            n2 = 0;
                        }
                        chatList.remove(j);
                    }
                }
                un.setType(n2 + 1);
                chatList.add(un);
            }
            Log.i("asynctask", "end:0");
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                Log.i("asynctask", "start:0");
                List<Msg> unreadList = new ArrayList<Msg>();
                unreadList = chatList;
                getunread = unreadList.size();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
