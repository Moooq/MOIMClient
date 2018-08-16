package com.jammy.mchsclient.activity;

import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.model.Msg;
import com.jammy.mchsclient.model.ReturnUser;
import com.jammy.mchsclient.model.UserInfo;
import com.jammy.mchsclient.socket.NetService;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.userOnLine;

public class AddContactsActivity extends AppCompatActivity {

    public static final String TAG = "AddContactsActivity";

    RelativeLayout rlSearchUser;
    Button btnSearchUser;
    Button btnAddUser;
    EditText etAddUser;
    ImageView ivSearchHead;
    ImageView ivSearchGender;
    TextView tvSearchName;
    TextView tvSearchId;
    ReturnUser returnUser;
    UserInfo searchUser = null;
    Msg applyMsg = null;

    private NetService netService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(R.string.add_contacts);

        rlSearchUser = (RelativeLayout)findViewById(R.id.rl_search_user);
        btnSearchUser = (Button)findViewById(R.id.btn_search_user);
        btnAddUser = (Button)findViewById(R.id.btn_add_user);
        etAddUser = (EditText)findViewById(R.id.et_add_user);
        etAddUser = (EditText)findViewById(R.id.et_add_user);
        ivSearchGender = (ImageView)findViewById(R.id.iv_search_gender);
        ivSearchHead = (ImageView)findViewById(R.id.iv_search_head);
        tvSearchId = (TextView)findViewById(R.id.tv_search_id);
        tvSearchName = (TextView)findViewById(R.id.tv_search_name);
        init();

    }

    void init(){
        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.SEARCH_USER)
                        .params("userInfo.username",etAddUser.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                returnUser = gson.fromJson(s, ReturnUser.class);
                                if (returnUser.getCode() == 200 && returnUser.getMsg().equals("SUCCESS")) {
                                    searchUser = returnUser.getData();
                                    rlSearchUser.setVisibility(View.VISIBLE);
                                    tvSearchId.setText(searchUser.getUsername());
                                    tvSearchName.setText(searchUser.getNickname());
                                    if(searchUser.getGender()==0){
                                        ivSearchGender.setImageResource(R.drawable.female);
                                    }
                                    Picasso.with(getBaseContext())
                                            .load(API.HEAD_PATH+searchUser.getUsername()+"_Head.png")
                                            .fit()
                                            .error(R.drawable.head)
                                            .into(ivSearchHead);

                                        btnAddUser.setOnClickListener(

                                                new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                applyMsg.setReceiver(returnUser.getData()
                                                        .getUsername());
                                                applyMsg.setSender(userOnLine.getUsername());
                                                applyMsg.setMessagetype(Msg.APPLY);
                                                applyMsg.setType(-1);
                                                Log.i(TAG, "userOnline: "+userOnLine.getUsername());
                                                new SendApplication().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                            }
                                        });

                                }
                            }
                        });
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class SendApplication extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            Log.i("sendMessage", "doinbackground");
            netService = NetService.getInstance();
            if (!netService.isConnected()) {

                return 0;
            }
            Log.i("sendMessage", "setconnection+send");
            netService.send(applyMsg);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i("sendMessage", "onpostexecute:" + integer);
            if (integer == 0) {
                Toast.makeText(AddContactsActivity.this, getResources().getString(R.string.ERROR_INTERNET), Toast.LENGTH_SHORT).show();
            } else if (integer == 1) {
                Toast.makeText(AddContactsActivity.this, getResources().getString(R.string.send_success), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
