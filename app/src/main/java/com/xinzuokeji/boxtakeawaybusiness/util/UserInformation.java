package com.xinzuokeji.boxtakeawaybusiness.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.entities.LoginInfo;

import java.util.Map;


//用户信息的存储和获取
public class UserInformation {
    /***************************************************************************
     * 存储和获取用户信息 -- SharedPreference
     ***************************************************************************/
    // 用户信息
    private final static String userInformation = "USER_INFORMATION";
    // 初始化User
    private final static LoginInfo userInfo = new LoginInfo();
    // 获取共享信息
    private static SharedPreferences sharedPreferences = GSApplication
            .getInstance().getSharedPreferences(userInformation,
                    GSApplication.MODE_PRIVATE);

    // 获取当前存储的用户信息
    public static LoginInfo getUserInfo() {
        // 获取存储的信息
        Map<String, ?> objects = sharedPreferences.getAll();
        userInfo.code = (String) objects.get("code");
        userInfo.rmsg = (String) objects.get("rmsg");
        userInfo.user_id = (String) objects.get("user_id");
        userInfo.xtlen = (String) objects.get("xtlen");
//        userInfo.Tel = (String) objects.get("Tel");
//        userInfo.IsAuth = (Integer) objects.get("IsAuth");
//        userInfo.Token = (String) objects.get("Token");
        return userInfo;
    }

    // 修改当前存储的用户信息
    public static void setUserInfo(LoginInfo user) {
        Editor editor = sharedPreferences.edit();
        // 存储信息
        editor.putString("user_id", user.user_id);
        editor.putString("xtlen", user.xtlen);
        editor.putString("rmsg", user.rmsg);
//        editor.putString("Pid", user.Pid);
//        editor.putString("Tel", user.Tel);
//        editor.putInt("IsAuth", user.IsAuth);
//        editor.putString("Token", user.Token);
        editor.commit();
    }

    public static void setUser(String rmsg) {
        Editor editor = sharedPreferences.edit();
        // 存储信息
//        editor.putString("rmsg", LoginInfo.rmsg);

//        editor.putString("Pid", user.Pid);
//        editor.putString("Tel", user.Tel);
//        editor.putInt("IsAuth", user.IsAuth);
//        editor.putString("Token", user.Token);
        editor.commit();
    }

    //清除保存的用户信息
    public static void clearUserInfo() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
