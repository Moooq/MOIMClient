package com.jammy.mchsclient.userprofile;

import com.jammy.mchsclient.model.UserInfo;

import static com.jammy.mchsclient.MyApplication.userOnLine;

/**
 * Created by moqiandemac on 2018/3/7.
 */

public class UserInfoCase {
    public static void jammyCase(){
        UserInfo jammyInfo = new UserInfo();
        jammyInfo.setNickname("Jam");
        jammyInfo.setUsername("jammy");
        jammyInfo.setPassword("123");
        jammyInfo.setGender(1);
        jammyInfo.setPhone("13776548913");
        jammyInfo.setEmail("1042748078@qq.com");
        jammyInfo.setBirth("123");
        jammyInfo.setLocation("123");
        userOnLine = jammyInfo;
    }
    public static void martinCase(){
        UserInfo martinInfo = new UserInfo();
        martinInfo.setNickname("martin");
        martinInfo.setUsername("martin");
        martinInfo.setPassword("123");
        martinInfo.setGender(1);
        martinInfo.setPhone("11111111111");
        martinInfo.setEmail("123@qq.com");
        martinInfo.setBirth("12");
        martinInfo.setLocation("123");
        userOnLine = martinInfo;
    }
    public static void maoliCase(){
        UserInfo maoliInfo = new UserInfo();
        maoliInfo.setNickname("Maoli");
        maoliInfo.setUsername("maoli");
        maoliInfo.setPassword("123");
        maoliInfo.setGender(1);
        maoliInfo.setPhone("11111111110");
        maoliInfo.setEmail("111@qq.com");
        maoliInfo.setBirth("1");
        maoliInfo.setLocation("123");
        userOnLine = maoliInfo;
    }
}
