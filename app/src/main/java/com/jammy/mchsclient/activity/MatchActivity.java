package com.jammy.mchsclient.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jammy.mchsclient.R;

public class MatchActivity extends AppCompatActivity {

    Button btnMatch;
    AlertDialog dialog;
    TextView tvMatchingTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        btnMatch = (Button)findViewById(R.id.btn_match);

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(MatchActivity.this).inflate(R.layout.dialog_matching, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                //这个位置十分重要，只有位于这个位置逻辑才是正确的
                tvMatchingTime =(TextView) view.findViewById(R.id.tv_match_time);
                dialog = builder.show();
                final CountDownTimer timer = new CountDownTimer(60*1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                        tvMatchingTime.setText(""+millisUntilFinished/1000+"s");
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        Toast.makeText(MatchActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                    }
                }.start();
                view.findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭对话框
                        dialog.dismiss();
                        timer.onFinish();
                    }
                });
            }
        });

    }
}
