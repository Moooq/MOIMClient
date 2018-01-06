package com.jammy.mchsclient.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.ReturnSuccess;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.activityMap;
import static com.jammy.mchsclient.MyApplication.friends;
import static com.jammy.mchsclient.MyApplication.userOnLine;

public class InfoActivity extends AppCompatActivity {

    ImageView ivFriendHead, ivFriendGender;
    TextView tvFriendName, tvFriendID, tvFriendNick;
    Button btnFriendMessage, btnSetRemarks;

    public static Handler handler = null;
    Friend friend = new Friend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        activityMap.put("InfoActivity", this);
        ivFriendGender = (ImageView) findViewById(R.id.iv_info_gender);
        ivFriendHead = (ImageView) findViewById(R.id.iv_info_head);
        tvFriendName = (TextView) findViewById(R.id.tv_info_name);
        tvFriendID = (TextView) findViewById(R.id.tv_info_username);
        tvFriendNick = (TextView) findViewById(R.id.tv_info_nickname);
        btnFriendMessage = (Button) findViewById(R.id.btn_info_messages);
        btnSetRemarks = (Button) findViewById(R.id.btn_setremarks);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    void init() {
        Intent intent = getIntent();
        ArrayList<String> list = (ArrayList) intent.getSerializableExtra("friend");
        friend.setUsername(list.get(0));
        friend.setEmail(list.get(1));
        friend.setPhone(list.get(2));
        friend.setHead(list.get(3));
        friend.setNickname(list.get(4));
        friend.setGender(Integer.parseInt(list.get(5)));
        friend.setRemark(list.get(6));
        if (friend.getGender() == 1) {
            ivFriendGender.setImageResource(R.drawable.male);
        } else {
            ivFriendGender.setImageResource(R.drawable.female);
        }
        tvFriendNick.setText(friend.getNickname());
        tvFriendID.setText(friend.getUsername());
        if (friend.getRemark().equals(" ")) {
            tvFriendName.setText(friend.getNickname());
        } else {
            tvFriendName.setText(friend.getRemark());
        }
        btnFriendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityMap.containsKey("ChatActivity")) {
                    Message msg = new Message();
                    msg.what = 1;
                    ChatActivity.handler.sendMessage(msg);
                }
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chat", friend.getUsername());
                intent.putExtras(bundle);
                startActivity(intent);
                activityMap.remove("InfoActivity");
                finish();
            }
        });
        btnSetRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRemarks();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        tvFriendName.setText((String) msg.obj);
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                activityMap.remove("InfoActivity");
                this.finish(); // back button
                break;
            case R.id.action_menu:
                selectMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void selectMenu() {
        Dialog mDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.bottom_dialog_info, null);
        //初始化视图
//        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
//        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
//        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

    private void setRemarks() {
        Dialog mDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.bottom_dialog_remarks, null);
        //初始化视图
//        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
//        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
//        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        final EditText etRemarks = (EditText) root.findViewById(R.id.et_dialog_remarks);
        etRemarks.setHint(friend.getRemark());
        root.findViewById(R.id.btn_dialog_remarks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.SET_REMARKS_API)
                        .params("remarks.username", userOnLine.getUsername())
                        .params("remarks.friendname", friend.getUsername())
                        .params("remarks.remarks", etRemarks.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                ReturnSuccess returnInfo = gson.fromJson(s, ReturnSuccess.class);
                                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("SUCCESS")) {
                                    Friend f = friends.get(friend.getUsername());
                                    friends.remove(friend.getUsername());
                                    friends.put(friend.getUsername(), f);
                                    Message mesg = new Message();
                                    mesg.what = 0;
                                    mesg.obj = etRemarks.getText().toString();
                                    handler.sendMessage(mesg);
                                    if (activityMap.containsKey("ChatActivity")) {
                                        Message mesg2 = new Message();
                                        mesg.what = 2;
                                        mesg.obj = etRemarks.getText().toString();
                                        ChatActivity.handler.sendMessage(mesg);
                                    }
                                    etRemarks.setText("");
                                    etRemarks.clearFocus();
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
                                    }
                                }
                            }
                        });
            }
        });
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

}
