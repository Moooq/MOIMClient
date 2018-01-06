package com.jammy.mchsclient.model;

/**
 * Created by moqiandemac on 2017/7/15.
 */

public class ReturnUnRead {
    private int code;
    private String msg;
    private Msg[] data;

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

    public Msg[] getData() {
        return data;
    }

    public void setData(Msg[] data) {
        this.data = data;
    }
}
