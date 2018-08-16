package com.jammy.mchsclient.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.adapter.MyRecyclerAdapter;
import com.jammy.mchsclient.fragment.ChatFragment;
import com.jammy.mchsclient.layout.MultiSwipeRefreshLayout;
import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.Moment;
import com.jammy.mchsclient.model.Msg;
import com.jammy.mchsclient.model.ReturnMoments;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.activityMap;
import static com.jammy.mchsclient.MyApplication.friends;
import static com.jammy.mchsclient.MyApplication.userOnLine;
import static com.lzy.okgo.OkGo.getContext;

public class CircleActivity extends AppCompatActivity {

    public static final String TAG = "CircleActivity";
    private RecyclerView rvCircle;
    private ArrayList<Moment> moments;
    private MyRecyclerAdapter recycleAdapter;
    private TextView tvMomentsName ;
    private ImageView ivMomentsHead;

    public static Handler handler = null;

    private ReturnMoments returnMoments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.moments);
        }
        rvCircle = (RecyclerView)findViewById(R.id.rv_circle);
        init();

    }

    void init(){

        tvMomentsName = (TextView)findViewById(R.id.tv_moments_user) ;
        ivMomentsHead = (ImageView)findViewById(R.id.iv_moments_userhead);
        Log.i(TAG, "useronline:"+userOnLine.getNickname());
        tvMomentsName.setText(userOnLine.getNickname());
        Picasso.with(this)
                .load(API.HEAD_PATH+userOnLine.getUsername()+"_Head.png")
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.head)
                .into(ivMomentsHead);
        moments = new ArrayList<>();
        initData();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
//                    new RefreshMoments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    LayoutSetAdapter();
                }
                if(msg.what==1){
                    initData();
                }
            }
        };
    }

    void initData(){
        OkGo.post(API.RECEIVE_MOMENTS)
                .params("userInfo.username", MyApplication.userOnLine.getUsername())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        returnMoments = gson.fromJson(s,ReturnMoments.class);
                        if (returnMoments.getCode() == 200 && returnMoments.getMsg().equals("SUCCESS")) {
                            for(int i =0;i<returnMoments.getData().length;i++){
                                moments.add(returnMoments.getData()[i]);
                                Log.i(TAG, "moment:"+returnMoments.getData()[i].getMomentcontent());
                            }
                            Message mesg = new Message();
                            mesg.what = 0;
                            handler.sendMessage(mesg);
                        }
                    }
                });
    }

    void LayoutSetAdapter(){
        recycleAdapter= new MyRecyclerAdapter(CircleActivity.this , moments );
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        rvCircle.addItemDecoration(new DividerItemDecoration(getBaseContext(),DividerItemDecoration.VERTICAL));
        //设置布局管理器
        rvCircle.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        rvCircle.setAdapter( recycleAdapter);
        //设置增加或删除条目的动画
        rvCircle.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_circle_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                activityMap.remove("CircleActivity");
                this.finish(); // back button
                break;
            case R.id.action_circle_publish:
                Intent intent = new Intent(this, PublishMomentActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class RefreshMoments extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            initData();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            LayoutSetAdapter();
        }
    }
}
