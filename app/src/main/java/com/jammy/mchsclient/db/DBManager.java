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
        cv.put(DBHelper.FIELD_SENDER, msg.getSender());
        cv.put(DBHelper.FIELD_RECEIVER,msg.getReceiver());
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
            cv.put(DBHelper.FIELD_SENDER, msgs[i].getSender());
            cv.put(DBHelper.FIELD_RECEIVER,msgs[i].getReceiver());
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
                +" where sender='"+msg.getSender()
                +"' and receiver='"+msg.getReceiver()
                +"' and time='"+msg.getTime()
                +"' and messageType="+msg.getMessagetype()
                +" and messageContent='"+msg.getMessagecontent()
                +"' and type="+msg.getType() ;
        db.execSQL(sql);
        db.close();
    }

    /**
     * 清空数据库
     */
    public void deleteDatas(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "delete from "+DBHelper.TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }

    /**
     * 建表
     */
    public void createTable(Context context){
//        deleteDatas(context);
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS " + MyApplication.userOnLine.getUsername() + "(id integer primary key autoincrement ,sender varchar(20),receiver varchar(20),time varchar(50),messageType integer(10),messageContent varchar(400),type int(10));";
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
        String[] columns = {DBHelper.FIELD_SENDER,DBHelper.FIELD_RECEIVER,DBHelper.FIELD_TIME,DBHelper.FIELD_MESSAGE_TYPE,DBHelper.FIELD_MESSAGE_CONTENT,DBHelper.FIELD_TYPE};
        //获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询数据库
        Cursor cursor = null;
        try {
            Log.i(TAG, "table_name:"+DBHelper.TABLE_NAME);
            cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, "sender");//获取数据游标
            while (cursor.moveToNext()) {
                Msg msg1 = new Msg();
                msg1.setSender(cursor.getString(0));
                msg1.setReceiver(cursor.getString(1));
                msg1.setTime(cursor.getString(2));
                msg1.setMessagetype(Integer.parseInt(cursor.getString(3)));
                msg1.setMessagecontent(cursor.getString(4));
                msg1.setType(Integer.parseInt(cursor.getString(5)));
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
        String[] columns = {DBHelper.FIELD_SENDER,DBHelper.FIELD_RECEIVER,DBHelper.FIELD_TIME,DBHelper.FIELD_MESSAGE_TYPE,DBHelper.FIELD_MESSAGE_CONTENT,DBHelper.FIELD_TYPE};
        //获取可读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询数据库
        Cursor cursor = null;
        try {
            cursor = db.query(DBHelper.TABLE_NAME, columns, "sender=?", new String[]{friendname}, null, null, null);//获取数据游标
            while (cursor.moveToNext()) {
                Msg msg1 = new Msg();
                msg1.setSender(cursor.getString(0));
                msg1.setReceiver(cursor.getString(1));
                msg1.setTime(cursor.getString(2));
                msg1.setMessagetype(Integer.parseInt(cursor.getString(3)));
                msg1.setMessagecontent(cursor.getString(4));
                msg1.setType(Integer.parseInt(cursor.getString(5)));
                msgList.add(msg1);
            }
            //关闭游标防止内存泄漏
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "queryData" + e.toString());
        }
        Cursor cursor2 = null;
        try {
            cursor2 = db.query(DBHelper.TABLE_NAME, columns, "receiver=?", new String[]{friendname}, null, null, null);//获取数据游标
            while (cursor2.moveToNext()) {
                Msg msg1 = new Msg();
                msg1.setSender(cursor2.getString(0));
                msg1.setReceiver(cursor2.getString(1));
                msg1.setTime(cursor2.getString(2));
                msg1.setMessagetype(Integer.parseInt(cursor2.getString(3)));
                msg1.setMessagecontent(cursor2.getString(4));
                msg1.setType(Integer.parseInt(cursor2.getString(5)));
                msgList.add(msg1);
            }
            //关闭游标防止内存泄漏
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "queryData" + e.toString());
        }
        //关闭数据库
        db.close();
        return msgList;
    }

}