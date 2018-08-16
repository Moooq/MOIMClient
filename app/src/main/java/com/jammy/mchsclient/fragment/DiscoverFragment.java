package com.jammy.mchsclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.CircleActivity;
import com.jammy.mchsclient.activity.MatchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class DiscoverFragment extends Fragment {

    RelativeLayout llMoments;
    TextView tvNew;
    ImageView ivNewHead;
    RelativeLayout rlMatch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View DiscoverView = inflater.inflate(R.layout.fragment_discover, container, false);

        ButterKnife.bind(getActivity());
        tvNew = (TextView)DiscoverView.findViewById(R.id.tv_moment_new);
        ivNewHead = (ImageView)DiscoverView.findViewById(R.id.iv_moments_head);
        tvNew.setVisibility(View.GONE);
        ivNewHead.setVisibility(View.GONE);
        rlMatch = (RelativeLayout)DiscoverView.findViewById(R.id.rl_match) ;
        llMoments = (RelativeLayout) DiscoverView.findViewById(R.id.ll_moments);
        llMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CircleActivity.class);
                startActivity(intent);
            }
        });
        rlMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MatchActivity.class);
                startActivity(intent);
            }
        });
        return DiscoverView;
    }
}
