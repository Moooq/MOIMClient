package com.jammy.mchsclient.model;

import java.io.Serializable;

/**
 * Created by moqiandemac on 2017/7/14.
 */

public class Msg implements Serializable{
    private static final long serialVersionUID = 2731980850540638565l;
    public static final int STRING = 1;
    public static final int PICTURE = 2;
    public static final int VOICE = 4;
    public static final int OTHER = 3;
    public static final int APPLY = 0;

    private Object messagecontent;
    private int messagetype;
    private String time;
    private String sender;
    private String receiver;
    private int type;

    public Object getMessagecontent() {
        return messagecontent;
    }
    public void setMessagecontent(Object messagecontent) {
        this.messagecontent = messagecontent;
    }
    public int getMessagetype() {
        return messagetype;
    }
    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {this.receiver = receiver;}
    public int getType() {return type;}
    public void setType(int type) {this.type = type;}
}
