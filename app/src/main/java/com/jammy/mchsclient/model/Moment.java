package com.jammy.mchsclient.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by moqiandemac on 2018/3/11.
 */

public class Moment implements Serializable {
    private String momentcontent;
    private String username;
    private String location;
    private String[] imgpaths;
    private String time;

    public String[] getImgpaths() {
        return imgpaths;
    }

    public void setImgpaths(String[] imgpaths) {
        this.imgpaths = imgpaths;
    }

    public String getMomentcontent() {
        return momentcontent;
    }

    public void setMomentcontent(String momentcontent) {
        this.momentcontent = momentcontent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
