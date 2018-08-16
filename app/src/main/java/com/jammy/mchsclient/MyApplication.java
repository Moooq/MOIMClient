package com.jammy.mchsclient;

import android.app.Activity;
import android.app.Application;

import com.jammy.mchsclient.model.Friend;
import com.jammy.mchsclient.model.UserInfo;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by moqiandemac on 2017/7/7.
 */

public class MyApplication extends Application {
    public static UserInfo userOnLine = new UserInfo();
    public static Map<String,Friend> friends= new HashMap<String,Friend>();
    public static Map<String,Activity> activityMap = new HashMap<String,Activity>();
//    public static Map<String,Integer> newmsg = new HashMap<String, Integer>();
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);
        try {
            OkGo.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    .setRetryCount(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
