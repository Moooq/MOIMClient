package com.jammy.mchsclient.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.LaunchActivity;
import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.OffLine;
import com.jammy.mchsclient.model.UserInfo;
import com.jammy.mchsclient.socket.NetService;

import java.util.HashMap;

import static com.jammy.mchsclient.MyApplication.activityMap;
import static com.jammy.mchsclient.MyApplication.friends;
import static com.jammy.mchsclient.MyApplication.userOnLine;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class MeFragment extends Fragment {

    Button btnLogout;
    private NetService netService = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View MeView = inflater.inflate(R.layout.fragment_me, container, false);

        btnLogout = (Button)MeView.findViewById(R.id.btn_logout);
        btnLogout.getBackground().setAlpha(220);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogOutTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
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
