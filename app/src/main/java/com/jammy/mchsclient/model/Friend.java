package com.jammy.mchsclient.model;

import java.io.Serializable;

/**
 * Created by moqiandemac on 2017/7/13.
 */

public class Friend implements Serializable{
    private String username;
    private String remark;
    private UserInfo friendinfo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UserInfo getFriendinfo() {
        return friendinfo;
    }

    public void setFriendinfo(UserInfo friendinfo) {
        this.friendinfo = friendinfo;
    }
}
