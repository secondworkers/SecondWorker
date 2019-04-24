package com.qiaoyi.secondworker.local;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.qiaoyi.secondworker.remote.UserBean;

import cn.isif.alibs.utils.SharePreferenceUtils;

/**
 * Created by le on 2018/3/18.
 */

public class AccountHandler {
  public enum LOGIN_TYPE{
    MOBILE,THIRD,NOBODY
  }

  public static LOGIN_TYPE getUserState() {
    UserBean ub = getUser();
    if (null==ub){
      return LOGIN_TYPE.NOBODY;
    }else if (!TextUtils.isEmpty(ub.mobile)){
      return LOGIN_TYPE.MOBILE;
    }else{
      return LOGIN_TYPE.THIRD;
    }
  }

  public static void logout(){
      SharePreferenceUtils.write(Config.LOCAL_STORE_USER_SESSION_FILE, Config.STORE_USER_KEY,
              "");
  }
  public static String checkLogin() {
    UserBean ub = getUser();
    if (null==ub){
      return null;
    }else{
      return ub.uid;
    }
  }

  public static void saveLoginInLocal(String session) {
    SharePreferenceUtils.write(Config.LOCAL_STORE_USER_SESSION_FILE, Config.STORE_USER_KEY,
        session);
  }

  public static UserBean getUser() {
    String session = SharePreferenceUtils.readString(Config.LOCAL_STORE_USER_SESSION_FILE,
        Config.STORE_USER_KEY);
    Gson gson = new Gson();
    UserBean person = gson.fromJson(session, UserBean.class);
    return person;
  }

  public static String getUserName() {
    UserBean ub = getUser();
    if (ub != null) {
      return ub.username;
    }
    return null;
  }
    public static String getUserId() {
    UserBean ub = getUser();
    if (ub != null) {
      return ub.uid;
    }
    return null;
  }
  public static String getUserPhone() {
    UserBean ub = getUser();
    if (ub != null) {
      return ub.mobile;
    }
    return "";
  }
  public static String getUserAavatar(){
    UserBean ub = getUser();
    if (ub != null) {
      return ub.avatar;
    }
    return "";
  }
}
