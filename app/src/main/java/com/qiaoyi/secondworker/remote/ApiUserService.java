package com.qiaoyi.secondworker.remote;


import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.IfOkNet;
import com.qiaoyi.secondworker.net.ServiceCallBack;

import cn.isif.ifok.Params;
import okhttp3.Call;

public class ApiUserService {
    //登录
    public static Call login(String mobile, String smscode, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("mobile", mobile);
        params.put("smscode", smscode);
        return IfOkNet.getInstance().post(Contact.LOGIN, params, callBack);
    }

    //第三方登录
    public static Call loginThird(String openId, String typeId, String nickname,String avatar,
                                  ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("openId", openId);
        params.put("typeId", typeId);
        params.put("nickname", nickname);
        params.put("avatar", avatar);
        return IfOkNet.getInstance().post(Contact.LOGIN, params, callBack);
    }

    //绑定第三方登录
    public static Call bindThird(String mobile, String smscode, String openId, String typeId,
                                 String nickname, String avatar,String isBindingThirdParty,ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("mobile", mobile);
        params.put("smscode", smscode);
        params.put("openId", openId);
        params.put("typeId", typeId);
        params.put("nickname", nickname);
        params.put("avatar", avatar);
        params.put("isBindingThirdParty", isBindingThirdParty);
        return IfOkNet.getInstance().post(Contact.LOGIN, params, callBack);
    }

    //发送短息
    public static Call sendSms(String mobile, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("phone", mobile);
        return IfOkNet.getInstance().post(Contact.SEND_SMS, params, callBack);
    }

    //第三方登录
    public static Call thirdLogin(String openId, String typeId, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("openId", openId);
        params.put("typeId", typeId);
        return IfOkNet.getInstance().post(Contact.THIRD_LOGIN, params, callBack);
    }

}

