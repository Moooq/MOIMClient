package com.jammy.mchsclient.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.db.DBManager;
import com.jammy.mchsclient.fragment.ChatFragment;
import com.jammy.mchsclient.model.Msg;
import com.jammy.mchsclient.socket.NetService;
import com.jammy.mchsclient.url.API;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.jammy.mchsclient.MyApplication.activityMap;
import static com.jammy.mchsclient.MyApplication.friends;
import static com.jammy.mchsclient.MyApplication.userOnLine;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";
    String friendName = null;
    DBManager dbManager = new DBManager();
    List<Msg> chatLog = new ArrayList<Msg>();
    ChatLogBaseAdapter chatLogBaseAdapter;
    Msg sendm = new Msg();
    public static Handler handler = null;

    ListView lvChatlog;
    int x = 0;
    Button btnSend;
    EditText etSendText;
    ImageView ivChatOther;

    private NetService netService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        friendName = (String) intent.getSerializableExtra("chat");
        activityMap.put("ChatActivity", this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!friends.get(friendName).getRemark().equals(" ")) {
                actionBar.setTitle(friends.get(friendName).getRemark());
            } else {
                actionBar.setTitle(friends.get(friendName).getFriendinfo().getNickname());
            }
        }

        lvChatlog = (ListView) findViewById(R.id.msg_list_view);
        btnSend = (Button) findViewById(R.id.send_btn);
        etSendText = (EditText) findViewById(R.id.input_text);
        ivChatOther = (ImageView) findViewById(R.id.iv_other);
        etSendText.addTextChangedListener(mTextWatcher);
        Log.i("friendname", "" + friendName);
        init();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = -1;
                Log.i("sendMessage", "onClick:send");
                Msg sendMsg = new Msg();
                sendMsg.setReceiver(friends.get(friendName).getUsername());
                sendMsg.setMessagetype(1);
                sendMsg.setType(0);
                sendMsg.setMessagecontent(etSendText.getText().toString());
                Log.i("sendMessage", "" + sendMsg.getMessagecontent());
                sendMsg.setSender(userOnLine.getUsername());
                sendMsg.setTime(getCurrentTime());
                Log.i("sendMessage", "onClick:send2");
                sendMessage(sendMsg);
                etSendText.setText("");
                etSendText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });
    }

    void init() {
        setTitle(friendName);
        chatLog = dbManager.queryFriendMessageDatas(getBaseContext(), friendName);
        chatLog = sortStringMethod(chatLog);
        Log.i("chatlogsize", ""+chatLog.size());
        if (chatLog.size() > 0) {
            chatLogBaseAdapter = new ChatLogBaseAdapter(chatLog);
            lvChatlog.setAdapter(chatLogBaseAdapter);
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
//                        if(chatLog.size()==1){
                            chatLogBaseAdapter = new ChatLogBaseAdapter(chatLog);
                            lvChatlog.setAdapter(chatLogBaseAdapter);
//                        }
                        chatLog.add((Msg) msg.obj);
                        Log.i("sendMessage", "123");
                        chatLogBaseAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        activityMap.remove("ChatActivity");
                        finish();
                        break;
                    case 2:
                        ActionBar actionBar1 = getSupportActionBar();
                        actionBar1.setTitle(friends.get(friendName).getRemark());
                        break;
                }
            }
        };

        lvChatlog.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                MenuInflater menuInflater = getMenuInflater();
//                menuInflater.inflate(R.menu.menu_msg, menu);
                menu.add(0, 0, 0, "copy");
                menu.add(0, 1, 0, "delete");
            }
        });
    }

    //msg按时间排序
    public  List<Msg> sortStringMethod(List<Msg> list){
        Collections.sort(list, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                Msg m1=(Msg) o1;
                Msg m2=(Msg) o2;
                return m1.getTime().compareTo(m2.getTime());
            }
        });
        return list;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        switch (item.getItemId()) {
            case 0:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(chatLog.get(info.position).getMessagecontent().toString());
                return true;
            case 1:
                dbManager.deleteData(chatLog.get(info.position),getBaseContext());
                chatLog.remove(info.position);
                Log.i(TAG, "chatlog.size_after deleted:"+chatLog.size());
                chatLogBaseAdapter.notifyDataSetChanged();
                return true;
            default:
            return super.onContextItemSelected(item);}
    }

    public void sendMessage(final Msg m) {
        //sendm = new Msg();
        Log.i("sendMessage", "sendMessage: ");
        sendm = m;
//        Log.i("sendMessage", "2:" + sendm.getType());
        new SendMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    private class ChatLogBaseAdapter extends BaseAdapter {
        private List<Msg> data = new ArrayList<Msg>();

        private ChatLogBaseAdapter(List<Msg> list) {
            this.data = list;
//            Log.i("sendMessage", "56：" + list.size() + ":" + data.get(data.size() - 1).getType() + ":" + chatLog.get(chatLog.size() - 1).getType());
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_msg, null);
            final LinearLayout layoutLeft = (LinearLayout) view.findViewById(R.id.left_layout);
            final LinearLayout layoutLeftMsg = (LinearLayout) view.findViewById(R.id.layout_left_msg);
            final LinearLayout layoutRightMsg = (LinearLayout) view.findViewById(R.id.layout_right_msg);
            final LinearLayout layoutRight = (LinearLayout) view.findViewById(R.id.right_layout);
            final TextView tvLeftTime = (TextView) view.findViewById(R.id.receive_time);
            final TextView tvRightTime = (TextView) view.findViewById(R.id.send_time);
            final TextView tvLeftContent = (TextView) view.findViewById(R.id.left_msg);
            final TextView tvRightContent = (TextView) view.findViewById(R.id.right_msg);
            final ImageView ivLeftContent = (ImageView) view.findViewById(R.id.left_msg_img);
            final ImageView ivRightContent = (ImageView) view.findViewById(R.id.right_msg_img);
            final ImageView ivLeftHead = (ImageView) view.findViewById(R.id.receive_head);
            final ImageView ivRightHead = (ImageView) view.findViewById(R.id.send_head);
            final ImageView ivRightFail = (ImageView) view.findViewById(R.id.send_fail);
//            Log.i("sendMessage", "5：" + position + ":" + data.get(position).getType() + ":" + chatLog.get(position).getType() + ":" + x);
            Log.i("ChatLogBaseAdapter", "datasize:"+data.size());
            Log.i("ChatLogBaseAdapter", "position:"+position);
            Picasso.with(getBaseContext())
                    .load(API.HEAD_PATH+userOnLine.getUsername()+"_Head.png")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.head)
                    .into(ivRightHead);
            Picasso.with(getBaseContext())
                    .load(API.HEAD_PATH+friendName+"_Head.png")
                    .fit()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.head)
                    .into(ivLeftHead);
            if ((position == data.size() - 1) && x == -1) {
                Msg cm = chatLog.get(position);
                chatLog.remove(position);
                chatLog.add(cm);
                Log.i("sendMessage", "x：" + x + ":" + position);
                layoutRight.setVisibility(View.VISIBLE);
                layoutLeft.setVisibility(View.GONE);
                tvRightTime.setText(data.get(position).getTime());
                if (data.get(position).getMessagetype() == 1) {
                    tvRightContent.setText(data.get(position).getMessagecontent() + "");
                    ivRightContent.setVisibility(View.GONE);
                }
                ivRightFail.setVisibility(View.GONE);
                x = 0;
            } else {
                if (data.get(position).getSender().equals(userOnLine.getUsername())) {
                    Log.i("chatlog", "sender");
                    layoutRight.setVisibility(View.VISIBLE);
                    layoutLeft.setVisibility(View.GONE);
                    tvRightTime.setText(data.get(position).getTime());
                    if (data.get(position).getMessagetype() == Msg.STRING) {
                        tvRightContent.setText(data.get(position).getMessagecontent() + "");
                        ivRightContent.setVisibility(View.GONE);
                    }
                    ivRightFail.setVisibility(View.GONE);

                } else {
                    Log.i("chatlog", "receiver");
                    layoutRight.setVisibility(View.GONE);
                    layoutLeft.setVisibility(View.VISIBLE);
                    tvLeftTime.setText(data.get(position).getTime());
                    if (data.get(position).getMessagetype() == Msg.STRING) {
                        tvLeftContent.setText(data.get(position).getMessagecontent() + "");
                        ivLeftContent.setVisibility(View.GONE);
                    }
                }
            }
            return view;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                activityMap.remove("ChatActivity");
                this.finish(); // back button
                break;
            case R.id.action_chat_info:
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                ArrayList<String> finfo = new ArrayList<String>();
                finfo.add(friends.get(friendName).getUsername());
                finfo.add(friends.get(friendName).getFriendinfo().getEmail());
                finfo.add(friends.get(friendName).getFriendinfo().getPhone());
                finfo.add(friends.get(friendName).getFriendinfo().getHead());
                finfo.add(friends.get(friendName).getFriendinfo().getNickname());
                finfo.add(friends.get(friendName).getFriendinfo().getGender() + "");
                finfo.add(friends.get(friendName).getRemark());
                Bundle bundle = new Bundle();
                bundle.putSerializable("friend", finfo);
                Intent intent = new Intent(this, InfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            activityMap.remove("ChatActivity");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class SendMessage extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            Log.i("sendMessage", "doinbackground");
            netService = NetService.getInstance();
            if (!netService.isConnected()) {

                return 0;
            }
            Log.i("sendMessage", "setconnection+send");
            netService.send(sendm);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i("sendMessage", "onpostexecute:" + integer);
            if (integer == 0) {
                Toast.makeText(ChatActivity.this, getResources().getString(R.string.ERROR_INTERNET), Toast.LENGTH_SHORT).show();
            } else if (integer == 1) {
                Message mesg = new Message();
                mesg.what = 0;
                mesg.obj = sendm;
                handler.sendMessage(mesg);
                Message mesg2 = new Message();
                mesg2.what = 0;
                mesg2.obj = sendm;
//                Log.i("sendMessage", "3:" + sendm.getType());
                ChatFragment.handler.sendMessage(mesg2);
            }
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (temp.length() > 0) {
                ivChatOther.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
            } else {
                ivChatOther.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.GONE);
            }

        }
    };
}
