package com.jammy.mchsclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jammy.mchsclient.MyApplication;
import com.jammy.mchsclient.model.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moqiandemac on 2017/7/14.
 */

public class DBManager {
    private static final String TAG = "DatabaseManager";
    // 静态引用
    private volatile static DBManager mInstance;
    // DatabaseHelper



    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        DBManager inst = mInstance;
        if (inst == null) {
            synchronized (DBManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new DBManager();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 插入数据
     */
    public void insertData(Msg msg,Context context) {
        createTable(context);
        DBHelper dbHelper = new DBHelper(context);
        //获取写数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //生成要修改或者插入的键值
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_FRIEND, msg.getFriend());
        cv.put(DBHelper.FIELD_TIME, msg.getTime());
        cv.put(DBHelper.FIELD_MESSAGE_TYPE, msg.getMessagetype());
        cv.put(DBHelper.FIELD_MESSAGE_CONTENT, msg.getMessagecontent().toString());
        cv.put(DBHelper.FIELD_TYPE,msg.getType());
        // insert 操作
        db.insert(DBHelper.TABLE_NAME, null, cv);
        //关闭数据库
        db.close();
    }

    /**
     * 批量插入
     */
    public void insertDatas(Msg[] msgs,Context context) {
        createTable(context);
        DBHelper dbHelper = new DBHelper(context);
        //获取写数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        for (int i = 0; i < msgs.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FIELD_FRIEND, msgs[i].getFriend());
            cv.put(DBHelper.FIELD_TIME, msgs[i].getTime());
            cv.put(DBHelper.FIELD_MESSAGE_TYPE, msgs[i].getMessagetype());
            cv.put(DBHelper.FIELD_MESSAGE_CONTENT, msgs[i].getMessagecontent().toString());
            cv.put(DBHelper.FIELD_TYPE,msgs[i].getType());
            // insert 操作
            db.insert(DBHelper.TABLE_NAME, null, cv);
        }
        //关闭数据库
        db.close();
    }

    /**
     * 删除数据
     */
    public void deleteData(Msg msg,Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "delete from "+DBHelper.TABLE_NAME
                +" where friend='"+msg.getFriend()
                +"' and time='"+msg.getTime()
                +"' and messageType="+msg.getMessagetype()
                +" and messageContent='"+msg.getMessagecontent()
                +"' and type="+msg.getType();
        db.execSQL(sql);
        db.close();
    }

    /**
     * 建表
     */
    public void createTable(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS " + MyApplication.userOnLine.getUsername() + "(id integer primary key autoincrement ,friend varchar(20),time varchar(50),messageType integer(10),messageContent varchar(400),type int(10));";
        db.execSQL(sql);
        db.close();
    }

    /**
     * 查询全部数据
     */
    public List<Msg> queryDatas(Context context) {
        createTable(context);
        DBHelper dbHelper = new DBHelper(context);
        List<Msg> msgList=new ArrayList<Msg>();
        int i =0;
        //指定要查询的是哪几列数据
        String[] columns = {DBHelper.FIELD_FRIEND,DBHelper.FIELD_TIME,DBHelper.FIELD_MESSAGE_TYPE,DBHelper.FIELD_MESSAGE_CONTENT,DBHelper.FIELD_TYPE};
        //获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询数据库
        Cursor cursor = null;
        try {
            cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, "friend");//获取数据游标
            while (cursor.moveToNext()) {
                Msg msg1 = new Msg();
                msg1.setFriend(cursor.getString(0));
                Log.i("123", "friend"+cursor.getString(0));
                msg1.setTime(cursor.getString(1));
                msg1.setMessagetype(Integer.parseInt(cursor.getString(2)));
                msg1.setMessagecontent(cursor.getString(3));
                msg1.setType(Integer.parseInt(cursor.getString(4)));
                msgList.add(msg1);
            }
            //关闭游标防止内存泄漏
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "queryDatas" + e.toString());
        }
        //关闭数据库
        db.close();
        return msgList;
    }

    /**
     * 查询某人聊天数据
     */
    public List<Msg> queryFriendMessageDatas(Context context,String friendname) {
        createTable(context);
        DBHelper dbHelper = new DBHelper(context);
        List<Msg> msgList=new ArrayList<Msg>();
        int i =0;
        //指定要查询的是哪几列数据
        String[] columns = {DBHelper.FIELD_FRIEND,DBHelper.FIELD_TIME,DBHelper.FIELD_MESSAGE_TYPE,DBHelper.FIELD_MESSAGE_CONTENT,DBHelper.FIELD_TYPE};
        //获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询数据库
        Cursor cursor = null;
        try {
            cursor = db.query(DBHelper.TABLE_NAME, columns, "friend=?", new String[]{friendname}, null, null, "friend");//获取数据游标
            while (cursor.moveToNext()) {
                Msg msg1 = new Msg();
                msg1.setFriend(cursor.getString(0));
                Log.i("123", "friend"+cursor.getString(0));
                msg1.setTime(cursor.getString(1));
                msg1.setMessagetype(Integer.parseInt(cursor.getString(2)));
                msg1.setMessagecontent(cursor.getString(3));
                msg1.setType(Integer.parseInt(cursor.getString(4)));
                msgList.add(msg1);
            }
            //关闭游标防止内存泄漏
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "queryDatas" + e.toString());
        }
        //关闭数据库
        db.close();
        return msgList;
    }

}