package com.qiaoyi.secondworker.remote;


import android.text.TextUtils;

import com.qiaoyi.secondworker.bean.WrapAddressBean;
import com.qiaoyi.secondworker.bean.WrapPrePayOrderBean;
import com.qiaoyi.secondworker.bean.WrapWorkerBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.IfOkNet;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.pay.PrePayWeChatEntity;

import cn.isif.ifok.Params;
import okhttp3.Call;

public class ApiUserService {
    //登录
    public static Call login(String mobile, String smscode, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("phone", mobile);
        params.put("code", smscode);
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

    /**
     * 摇一摇 秒工人
     * @param counts
     * @param lng
     * @param lat
     * @param callBack
     * @return
     */
    public static Call shakeWorker(int counts, double lng,double lat, ServiceCallBack<WrapWorkerBean> callBack) {
        Params params = new Params.Builder().json().build();
        params.put("counts", counts);
        params.put("lng", lng);
        params.put("lat", lat);
        return IfOkNet.getInstance().post(Contact.SHAKE_WORKER, params, callBack);
    }

    /**
     * 地址列表
     * @param uid
     * @param callBack
     * @return
     */
    public static Call getUserAddressList(String uid, ServiceCallBack<WrapAddressBean> callBack) {
        Params params = new Params.Builder().json().build();
        params.put("uid", uid);
        return IfOkNet.getInstance().post(Contact.ADDRESS_LIST, params, callBack);
    }

    /**
     * 保存 修改地址
     * @param aid
     * @param uid
     * @param lat
     * @param lng
     * @param screenName
     * @param addressname
     * @param addressphone
     * @param addressDetailName
     * @param callBack
     * @return
     */
 public static Call updateAddressList(String aid,String uid,double lat,double lng,
                                      String screenName,String addressname,
                                      String addressphone,String addressDetailName,
                                      ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("aid", aid);
        params.put("uid", uid);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("screenName", screenName);
        params.put("addressname", addressname);
        params.put("addressphone", addressphone);
        params.put("addressDetailName", addressDetailName);
        return IfOkNet.getInstance().post(Contact.ADDRESS_UPDATE, params, callBack);
    }

    /**
     * 删除地址
     * @param aid
     * @param callBack
     * @return
     */
public static Call delAddress(String aid,ServiceCallBack callBack){
    Params params = new Params.Builder().json().build();
    params.put("aid", aid);
    return IfOkNet.getInstance().post(Contact.ADDRESS_DELETE, params, callBack);
}

    /**
     * 生成订单
     * @param addressId
     * @param serviceTime
     * @param uid
     * @param serviceLength
     * @param orgId
     * @param workId
     * @param callBack
     * @return
     */
public static Call createOrder(String addressId,String serviceTime,String uid,
                               String serviceLength,String orgId,String workId,
                               ServiceCallBack<WrapPrePayOrderBean> callBack){
    Params params = new Params.Builder().json().build();
    params.put("addressId", addressId);
    params.put("serviceTime", serviceTime);
    params.put("uid", uid);
    params.put("serviceLength", serviceLength);
    params.put("orgId", orgId);
    params.put("workId", workId);
    return IfOkNet.getInstance().post(Contact.CREATE_ORDER, params, callBack);
}

    /**
     * 点击去支付 根据订单id 获取 订单生成时间
     * @param callBack
     * @return
     */
public static Call getOrderInfo(String orderid,ServiceCallBack<WrapPrePayOrderBean> callBack){
    Params params = new Params.Builder().json().build();
    params.put("orderid",orderid);
    return IfOkNet.getInstance().post(Contact.ORDER_INFO, params, callBack);
}

    /**
     * 取消订单
     * @param orderid
     * @param status  删除 或者 取消订单
     * @param callBack
     * @return
     */
public static Call cancelAndDelOrder(String orderid,String status,ServiceCallBack callBack){
    Params params = new Params.Builder().json().build();
    params.put("orderid",orderid);
    if (TextUtils.equals(status,"delete")){
        return IfOkNet.getInstance().post(Contact.DEL_ORDER, params, callBack);
    }else if (TextUtils.equals(status,"cancel")){
        return IfOkNet.getInstance().post(Contact.CANCEL_ORDER, params, callBack);
    }
     return null;
}

public static Call wxPay(String orderid, double price , ServiceCallBack<PrePayWeChatEntity> callBack){
    Params params = new Params.Builder().json().build();
    params.put("orderid",orderid);
    params.put("price",price);
    return IfOkNet.getInstance().post(Contact.WX_PAY, params, callBack);
}
}

