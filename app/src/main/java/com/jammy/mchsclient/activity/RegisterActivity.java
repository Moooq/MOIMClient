package com.jammy.mchsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jammy.mchsclient.R;
import com.jammy.mchsclient.model.UserInfo;
import com.jammy.mchsclient.model.ReturnSuccess;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername,etPassword,etPhone,etEmail,etNickname,etBirth,etInfo,etLocation;
    RadioGroup rgroupGender;
    RadioButton rbGender;
    Button btnRgSubmit;
    ReturnSuccess returnSuccess;
    ImageView ivRegHead;
    int gender = 0;
    LinearLayout layoutReg1,layoutReg2,layoutReg3;
    RelativeLayout layoutHead;

    String imagePath;
    String newimagePath;
    String imageName;
    int x=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ivRegHead = (ImageView)findViewById(R.id.iv_reg_head);
        layoutReg1 = (LinearLayout)findViewById(R.id.layout_user_psw1);
        layoutReg2 = (LinearLayout)findViewById(R.id.layout_user_psw2);
        layoutReg3 = (LinearLayout)findViewById(R.id.layout_user_psw3);
        layoutReg1.getBackground().setAlpha(180);
        layoutReg2.getBackground().setAlpha(140);
        layoutReg3.getBackground().setAlpha(100);
        layoutHead = (RelativeLayout)findViewById(R.id.layout_head_nick);
        layoutHead.getBackground().setAlpha(220);
        etUsername = (EditText)findViewById(R.id.et_reg_username);
        etPassword = (EditText)findViewById(R.id.et_reg_password);
//        etPhone = (EditText)findViewById(R.id.et_reg_phone);
//        etEmail = (EditText)findViewById(R.id.et_reg_email);
        etNickname = (EditText)findViewById(R.id.et_reg_nickname);
//        etBirth = (EditText)findViewById(R.id.et_reg_birth);
//        etInfo = (EditText)findViewById(R.id.et_reg_info);
//        etLocation = (EditText)findViewById(R.id.et_reg_location);
        rgroupGender=(RadioGroup)findViewById(R.id.rgroup_gender);
        btnRgSubmit = (Button)findViewById(R.id.btn_rg_submit);
        rgroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                rbGender=(RadioButton)findViewById(rgroupGender.getCheckedRadioButtonId());
                if(rbGender.getId()==R.id.rb_gender_male){
                    gender =1;
                }else{
                    gender =0;
                }
                Log.i("register", "rgender:"+gender);
            }
        });

        ivRegHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=0;
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        btnRgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVLoadingIndicatorView regLoading = (AVLoadingIndicatorView)findViewById(R.id.reg_loading);
                regLoading.setVisibility(View.VISIBLE);
                if (x == 0) {
                    imageName = etUsername.getText().toString()+"_Head";
                    newimagePath = Environment.getExternalStorageDirectory() + "/" + imageName + ".png";
                    copyImage(imagePath, newimagePath);
                    OkGo.post(API.UPLOAD_HEAD_API)
                            .params("upload", new File(newimagePath))
                            .params("savePath", "c:\\headimage")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.i("sds", "onSuccess: ");
                                    deleteImage(newimagePath);
                                }
                            });
                }
                OkGo.post(API.REGISTER_API)
                        .params("userInfo.username",etUsername.getText().toString())
                        .params("userInfo.password",etPassword.getText().toString())
                        .params("userInfo.phone"," ")
                        .params("userInfo.email"," ")
                        .params("userInfo.nickname",etNickname.getText().toString())
                        .params("userInfo.birth"," ")
                        .params("userInfo.info"," ")
                        .params("userInfo.location"," ")
                        .params("userInfo.gender",gender+"")
                        .params("userInfo.head"," ")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.i("register", "gender:"+gender);
                                Gson gson = new Gson();
                                returnSuccess = gson.fromJson(s,ReturnSuccess.class);
                                Log.i("code",returnSuccess.getCode()+"");
                                if(returnSuccess.getCode()!=200&&!returnSuccess.getMsg().equals("SUCCESS")){
                                    Toast.makeText(RegisterActivity.this, "error!", Toast.LENGTH_SHORT).show();
                                }else{
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            Picasso.with(this)
                    .load(new File(imagePath))
                    .centerCrop()
                    .into((ImageView)findViewById(R.id.iv_reg_head));
            c.close();
        }
    }

    public void copyImage(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteImage(String PATH) {
        File file = new File(PATH);
        if (file.exists()) {
            file.delete();
        }
    }

}
