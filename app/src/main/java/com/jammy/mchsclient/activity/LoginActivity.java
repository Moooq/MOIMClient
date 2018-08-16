package com.jammy.mchsclient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.db.DBManager;
import com.jammy.mchsclient.model.ReturnUser;
import com.jammy.mchsclient.model.User;
import com.jammy.mchsclient.socket.NetService;
import com.jammy.mchsclient.url.API;
import com.jammy.mchsclient.util.GetResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wang.avi.AVLoadingIndicatorView;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.userOnLine;

public class LoginActivity extends AppCompatActivity {


    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private RelativeLayout layoutUser, layoutPsw;
    private Button btnLogin;
    private Button btnRegister;
    private ReturnUser returnUser;
    private TextView tvLgUserInfo;
    private AVLoadingIndicatorView logLoading;
    private User u = new User();
    private NetService netService = null;
    private Handler handler = null;
    DBManager dbManager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        logLoading = (AVLoadingIndicatorView) findViewById(R.id.log_loading);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        layoutUser = (RelativeLayout) findViewById(R.id.layout_user);
        layoutPsw = (RelativeLayout) findViewById(R.id.layout_psw);
        layoutUser.getBackground().setAlpha(150);
        layoutPsw.getBackground().setAlpha(150);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.getBackground().setAlpha(150);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//                }
                OkGo.post(API.LOGIN_API)
                        .params("userInfo.username", etUsername.getText().toString())
                        .params("userInfo.password", etPassword.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson = new Gson();
                                returnUser = gson.fromJson(s, ReturnUser.class);
                                if (returnUser.getCode() == 200 && returnUser.getMsg().equals("SUCCESS")) {
                                    u.setPassword(etPassword.getText().toString());
                                    u.setUsername(etUsername.getText().toString());
                                    userOnLine = returnUser.getData();
                                    tryLogin();
                                } else {
                                    Toast.makeText(LoginActivity.this, "error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //case
//                if(etUsername.getText().toString().equals("jammy"))
//                    UserInfoCase.jammyCase();
//                if(etUsername.getText().toString().equals("martin"))
//                    UserInfoCase.martinCase();
//                if(etUsername.getText().toString().equals("maoli"))
//                    UserInfoCase.maoliCase();
//                tryLogin();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                finish();
//                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    public void tryLogin() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                logLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Integer doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                netService = NetService.getInstance();
                netService.closeConnection();
                netService.setConnection(getBaseContext(), handler);
                if (!netService.isConnected()) {
                    Log.i(TAG, "netService:connect fail");
                    return 0;
                }
                netService.send(u);
                int count = 0;
                while (!GetResult.isReceived() && count < 10) {
                    count++;
                }
                GetResult.setReceived(false);
                Log.i(TAG, "getResult: "+GetResult.getIResult());
                Log.i(TAG, "isReceived:"+GetResult.isReceived());
                if (GetResult.getIResult() == 0) {
                    return 1;
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                logLoading.setVisibility(View.GONE);
                super.onPostExecute(result);
                if (result == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.ERROR_LOGIN), Toast.LENGTH_LONG).show();
                } else if (result == 1) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        }.execute();
    }

}
