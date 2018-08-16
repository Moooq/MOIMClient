package com.jammy.mchsclient.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by moqiandemac on 2018/3/11.
 */

public class Comment implements Serializable {

    private String commentcontent;
    private String username;
    private String commented;
    private Date time;
    private int pid;

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommented() {
        return commented;
    }

    public void setCommented(String commented) {
        this.commented = commented;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
