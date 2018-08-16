package com.jammy.mchsclient.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.model.ReturnSuccess;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.userOnLine;

public class PublishMomentActivity extends AppCompatActivity {


    public static final String TAG = "PublishMomentActivity";
    EditText etMoment;
    ReturnSuccess returnSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_moment);
        etMoment = (EditText)findViewById(R.id.et_moment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                break;
            case R.id.item_publish:
                if(etMoment.getText().toString().equals("")){
                    Toast.makeText(this, R.string.say_something, Toast.LENGTH_SHORT).show();
                }else{
                    Date curDate = new Date(System.currentTimeMillis());
                    Log.i(TAG, "moment:"+ userOnLine.getUsername()+":time:"+getCurrentTime());
                    final String stime = getCurrentTime();
                    OkGo.post(API.PUBLISH_MOMENT)
                            .params("moment.momentcontent",etMoment.getText().toString())
                            .params("moment.username", userOnLine.getUsername())
                            .params("moment.time",stime)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Gson gson = new Gson();
                                    returnSuccess = gson.fromJson(s, ReturnSuccess.class);
                                    Log.i("code", returnSuccess.getCode() + "");
                                    if (returnSuccess.getCode() != 200 && !returnSuccess.getMsg().equals("SUCCESS")) {
                                        Toast.makeText(PublishMomentActivity.this, "error!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Message mesg = new Message();
                                        mesg.what = 1;
                                        CircleActivity.handler.sendMessage(mesg);
                                        finish();
                                    }
                                }
                            });
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }
}
