package com.jammy.mchsclient.model;

import java.io.Serializable;

/**
 * Created by moqiandemac on 2017/7/14.
 */

public class Msg implements Serializable{
    private static final long serialVersionUID = 2731980850540638565l;
    private String messagecontent;
    private int messagetype;
    private int type=-1;
    private String time;
    private String friend;
    private String username;

    public Msg(String messagecontent, int messagetype, int type, String time, String friend, String username) {
        this.messagecontent = messagecontent;
        this.messagetype = messagetype;
        this.type = type;
        this.time = time;
        this.friend = friend;
        this.username = username;
    }

    public Msg(){

    }

    public Object getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public int getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
