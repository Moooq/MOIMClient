package com.jammy.mchsclient.url;

/**
 * Created by moqiandemac on 2017/7/6.
 */

public class API {

    public static String Http = "http://";
    public static String IP = "192.168.43.155";//redian
//    public static String IP = "192.168.1.105";
    public static String LOGIN_API = Http+IP+":8080/MCHATServer/userInfo/loginAction!selectUserInfo";
    public static String REGISTER_API=Http+IP+":8080/MCHATServer/userInfo/registerAction!addUserInfo";
    public static String RECEIVE_FRIENDS_API=Http+IP+":8080/MCHATServer/userInfo/friendsAction!receiveFriends";
    public static String RECEIVE_UNREAD_API=Http+IP+":8080/MCHATServer/userInfo/unReadAction!receiveUnreadMessage";
    public static String SET_REMARKS_API = Http+IP+":8080/MCHATServer/userInfo/setRemarksAction!setRemarks";
    public static String UPLOAD_IMAGE_API = Http+IP+":8080/MCHATServer/userInfo/imgAction!uploadImage";

    public static String SEARCH_USER = Http+IP+":8080/MCHATServer/userInfo/friendsAction!searchUser";

    public static String HEAD_PATH = Http+IP+":8080/MCHATServer/image/head/";


    public static String PUBLISH_MOMENT = Http+IP+":8080/MCHATServer/userInfo/momentsAction!publishMoment";
    public static String RECEIVE_MOMENTS = Http+IP+":8080/MCHATServer/userInfo/momentsAction!receiveMoments";
}
