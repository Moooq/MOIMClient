package com.jammy.mchsclient.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jammy.mchsclient.MyApplication;

import static com.jammy.mchsclient.MyApplication.userOnLine;

/**
 * Created by moqiandemac on 2017/7/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "moim";//数据库名字
    public static String TABLE_NAME= MyApplication.userOnLine.getUsername();// 表名
    public static String  FIELD_SENDER= "sender";// 列名
    public static String  FIELD_RECEIVER= "receiver";// 列名
    public static String  FIELD_TIME= "time";// 列名
    public static String  FIELD_MESSAGE_TYPE= "messageType";// 列名
    public static String  FIELD_MESSAGE_CONTENT= "messageContent";// 列名
    public static String  FIELD_TYPE= "type";// 列名
    private static final int DB_VERSION = 1;   // 数据库版本

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i(TAG, "DBHelper:name :"+MyApplication.userOnLine.getUsername());
    }

    /**
     * 创建数据库
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: database");
        //创建表
        String sql = "CREATE TABLE IF NOT EXISTS " + MyApplication.userOnLine.getUsername() + "(id integer primary key auto_increment ,sender varchar(20),receiver varchar(20),time varchar(50),messageType integer(10),messageContent varchar(400),type int(10));";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + userOnLine.getUsername() + "Error" + e.toString());
            return;
        }
    }

    /**
     * 数据库升级
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}