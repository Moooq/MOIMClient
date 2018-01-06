package com.jammy.mchsclient.util;

/**
 * Created by moqiandemac on 2017/7/13.
 */

public class GetResult {
    private static boolean isGetPassword = false;
    private static String password = null;
    public static boolean isGetPassword() {
        return GetResult.isGetPassword;
    }
    public static void setGetPassword(boolean isGetPassword) {
        GetResult.isGetPassword = isGetPassword;
    }
    public static void setPassword(String password){
        GetResult.password = password;
    }
    public static String getPassword(){
        return GetResult.password;
    }

    private static boolean isGetInformation = false;
    public static boolean isGetInformation(){
        return GetResult.isGetInformation;
    }
    public static void setGetInformation(boolean tag){
        GetResult.isGetInformation = tag;
    }

    private static int getInformationType = 0;
    public static int getGetInformationType() {
        return GetResult.getInformationType;
    }
    public static void setGetInformationType(int getInformationType) {
        GetResult.getInformationType = getInformationType;
    }


    private static boolean isRegister = true;
    public static boolean isRegister() {
        return GetResult.isRegister;
    }
    public static void setRegister(boolean isRegister) {
        GetResult.isRegister = isRegister;
    }




    private int iResult = 0;
    private boolean isReceived = false;
    private static GetResult getResult = null;

    private GetResult() {
        // TODO Auto-generated constructor stub
    }
    static {
        getResult = new GetResult();
    }
    public static int getIResult() {
        return getResult.iResult;
    }

    public void setIResult(int result) {
        this.iResult = result;
    }

    public static boolean isReceived(){
        return getResult.isReceived;
    }
    public static void setReceived(boolean a){
        getResult.isReceived = a;
    }


}
