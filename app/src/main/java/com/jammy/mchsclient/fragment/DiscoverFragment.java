package com.jammy.mchsclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.CircleActivity;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class DiscoverFragment extends Fragment {

    Button btnCircle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View DiscoverView = inflater.inflate(R.layout.fragment_discover, container, false);
        btnCircle = (Button)DiscoverView.findViewById(R.id.btn_circle);
        btnCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CircleActivity.class);
                startActivity(intent);
            }
        });
        return DiscoverView;
    }
}
