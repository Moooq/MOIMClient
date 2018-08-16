package com.jammy.mchsclient.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.LaunchActivity;
import com.jammy.mchsclient.activity.MyprofileActivity;
import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.OffLine;
import com.jammy.mchsclient.model.UserInfo;
import com.jammy.mchsclient.socket.NetService;
import com.jammy.mchsclient.url.API;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.jammy.mchsclient.MyApplication.activityMap;
import static com.jammy.mchsclient.MyApplication.friends;
import static com.jammy.mchsclient.MyApplication.userOnLine;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class MeFragment extends Fragment {

    Button btnLogout;
    TextView tvMeName;
    TextView tvMeId;
    ImageView ivMeGender;
    ImageView ivMeHead;
    LinearLayout llMeProfile;
    private NetService netService = null;
    public static Handler handler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View MeView = inflater.inflate(R.layout.fragment_me, container, false);

        llMeProfile = (LinearLayout)MeView.findViewById(R.id.me_profile);
        tvMeId = (TextView)MeView.findViewById(R.id.tv_me_username);
        tvMeName = (TextView)MeView.findViewById(R.id.tv_me_name);
        ivMeGender = (ImageView)MeView.findViewById(R.id.iv_me_gender);
        ivMeHead = (ImageView)MeView.findViewById(R.id.iv_me_head);
        tvMeName.setText(MyApplication.userOnLine.getNickname());
        tvMeId.setText(MyApplication.userOnLine.getUsername());
        Picasso.with(getContext())
                .load(API.HEAD_PATH+userOnLine.getUsername()+"_Head.png")
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.head)
                .into(ivMeHead);
        if(MyApplication.userOnLine.getGender()==0)
            ivMeGender.setImageResource(R.drawable.female);
        btnLogout = (Button)MeView.findViewById(R.id.btn_logout);
        btnLogout.getBackground().setAlpha(220);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogOutTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        llMeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyprofileActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler(){
            /**
             * Subclasses must implement this to receive messages.
             *
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Picasso.with(getContext())
                                .load(API.HEAD_PATH+userOnLine.getUsername()+"_Head.png")
                                .fit()
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .error(R.drawable.head)
                                .into(ivMeHead);
                        break;
                }
            }
        };

        return MeView;
    }
    class LogOutTask extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            netService = NetService.getInstance();
            OffLine offLine=new OffLine();
            offLine.setName(userOnLine.getUsername());
            if (!netService.isConnected()) {
                return 0;
            }
            netService.send(offLine);
            return 1;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer==1){
                Intent intent =new Intent(getActivity().getBaseContext(), LaunchActivity.class);
                userOnLine = null;
                friends = new HashMap<String,Friend>();
                activityMap = new HashMap<String,Activity>();
                startActivity(intent);
                getActivity().finish();
            }
        }
    }


}
