package com.jammy.mchsclient.model;

import java.util.List;

/**
 * Created by moqiandemac on 2018/3/11.
 */

public class ReturnMoments {
    private int code;
    private String msg;
    private Moment[] data;

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

    public Moment[] getData() {
        return data;
    }

    public void setData(Moment[] data) {
        this.data = data;
    }
}
