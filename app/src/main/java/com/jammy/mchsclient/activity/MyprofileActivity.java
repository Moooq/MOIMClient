package com.jammy.mchsclient.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jammy.mchsclient.R;
import com.jammy.mchsclient.fragment.MeFragment;
import com.jammy.mchsclient.model.Img;
import com.jammy.mchsclient.url.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

import static com.jammy.mchsclient.MyApplication.activityMap;
import static com.jammy.mchsclient.MyApplication.userOnLine;

public class MyprofileActivity extends AppCompatActivity {

    private RelativeLayout rlMyprofilePhoto;
    private RelativeLayout rlMyprofileName;
    private RelativeLayout rlMyprofileId;
    private RelativeLayout rlMyprofileMore;
    private ImageView ivProfileHead;
    private TextView tvProfileId;
    private TextView tvProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        verifyStoragePermissions(this);

        rlMyprofileId = (RelativeLayout)findViewById(R.id.rl_myprofile_id);
        rlMyprofileMore = (RelativeLayout)findViewById(R.id.rl_myprofile_more);
        rlMyprofileName = (RelativeLayout)findViewById(R.id.rl_myprofile_name);
        rlMyprofilePhoto = (RelativeLayout)findViewById(R.id.rl_myprofile_photo);
        ivProfileHead = (ImageView)findViewById(R.id.iv_myprofile_head);
        tvProfileId = (TextView)findViewById(R.id.tv_myprofile_id);
        tvProfileName = (TextView)findViewById(R.id.tv_myprofile_name);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(R.string.my_profile);
        init();
    }

    void init(){
        Picasso.with(this)
                .load(API.HEAD_PATH+userOnLine.getUsername()+"_Head.png")
                .fit()
                .error(R.drawable.head)
                .into(ivProfileHead);
        tvProfileId.setText(userOnLine.getUsername());
        tvProfileName.setText(userOnLine.getNickname());

        rlMyprofilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath;
            imagePath = c.getString(columnIndex);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ((ImageView) findViewById(R.id.iv_myprofile_head)).setImageBitmap(bm);
            c.close();
            OkGo.post(API.UPLOAD_IMAGE_API)
                    .isMultipart(true)
                    .params("img.type", Img.HEAD)
                    .params("fimgname",userOnLine.getUsername()+"_Head.png")
                    .params("fimg", new File(imagePath))
                    .params("img.path", userOnLine.getUsername()+"_Head.png")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("uploadimg", "onSuccess: ");
                            Message mesg = new Message();
                            mesg.what = 1;
                            MeFragment.handler.sendMessage(mesg);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
//                                    Log.e("upload-error",response.message()+","+response.code()+":"+response.body()+":"+response);
                        }
                    });
        }
    }

    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    private void verifyStoragePermissions(Activity activity) {
        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

}
