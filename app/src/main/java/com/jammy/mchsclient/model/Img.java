package com.jammy.mchsclient.model;

import java.io.File;

/**
 * Created by moqiandemac on 2018/1/11.
 */

public class Img {
    public static final int HEAD = 1;
    public static final int MOMENT = 2;
    private String path;
    private int type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
