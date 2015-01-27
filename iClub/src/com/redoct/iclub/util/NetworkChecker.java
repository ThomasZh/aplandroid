package com.redoct.iclub.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by liwenzhi on 14-12-11.
 */
public class NetworkChecker {

    private static  ConnectivityManager connectivityManager;

    public static void init(Context context){
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean available(){
        if(connectivityManager == null) return false;

        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        if (networkInfo != null && networkInfo.length > 0){
            for (int i = 0; i < networkInfo.length; i++){
//                System.out.println(i + "===状态===" + networkInfo[i].getState());
//                System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                // 判断当前网络状态是否为连接状态
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

}
