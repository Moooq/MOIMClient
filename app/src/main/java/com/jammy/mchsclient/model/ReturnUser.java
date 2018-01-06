package com.jammy.mchsclient.model;

/**
 * Created by moqiandemac on 2017/7/10.
 */

public class ReturnUser {
    private int code;
    private String msg;
    private UserInfo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
