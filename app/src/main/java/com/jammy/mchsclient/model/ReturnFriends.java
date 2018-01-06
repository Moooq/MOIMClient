package com.jammy.mchsclient.model;

/**
 * Created by moqiandemac on 2017/7/13.
 */

public class ReturnFriends {
    private int code;
    private String msg;
    private Friend[] data;

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

    public Friend[] getData() {
        return data;
    }

    public void setData(Friend[] data) {
        this.data = data;
    }
}
