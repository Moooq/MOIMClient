package com.jammy.mchsclient.url;

/**
 * Created by moqiandemac on 2017/7/6.
 */

public class API {

    public static String Http = "http://";
    public static String IP = "192.168.43.119";
//    public static String IP = "169.254.196.136";
    public static String LOGIN_API = Http+IP+":8080/MCHATServer/userInfo/loginAction!selectUserInfo";
    public static String REGISTER_API=Http+IP+":8080/MCHATServer/userInfo/registerAction!addUserInfo";
    public static String RECEIVE_FRIENDS_API=Http+IP+":8080/MCHATServer/userInfo/friendsAction!receiveFriends";
    public static String RECEIVE_UNREAD_API=Http+IP+":8080/MCHATServer/userInfo/unReadAction!receiveUnreadMessage";
    public static String SET_REMARKS_API = Http+IP+":8080/MCHATServer/userInfo/setRemarksAction!setRemarks";
    public static String UPLOAD_HEAD_API = Http+IP+":8080/MCHATServer/userInfo/uploadHeadAction!uploadHead";
}
