package com.qiaoyi.secondworker.remote;


import com.qiaoyi.secondworker.bean.ApplyBean;
import com.qiaoyi.secondworker.bean.PaymentDetailsBean;
import com.qiaoyi.secondworker.bean.WrapAddressBean;
import com.qiaoyi.secondworker.bean.WrapBankBean;
import com.qiaoyi.secondworker.bean.WrapCashBean;
import com.qiaoyi.secondworker.bean.WrapCommentBean;
import com.qiaoyi.secondworker.bean.WrapOrderBean;
import com.qiaoyi.secondworker.bean.WrapOrderDetailsBean;
import com.qiaoyi.secondworker.bean.WrapPaymentDetailsBean;
import com.qiaoyi.secondworker.bean.WrapPrePayOrderBean;
import com.qiaoyi.secondworker.bean.WrapPrePayWeChatEntity;
import com.qiaoyi.secondworker.bean.WrapQiNiuTokenBean;
import com.qiaoyi.secondworker.bean.WrapRequirementBean;
import com.qiaoyi.secondworker.bean.WrapRewardpointBean;
import com.qiaoyi.secondworker.bean.WrapUpdateBean;
import com.qiaoyi.secondworker.bean.WrapUserBean;
import com.qiaoyi.secondworker.bean.WrapWalletBean;
import com.qiaoyi.secondworker.bean.WrapWorkerBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.IfOkNet;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.pay.PrePayWeChatEntity;

import cn.isif.ifok.IfOk;
import cn.isif.ifok.Params;
import okhttp3.Call;

public class ApiUserService {
    //登录
    public static Call login(String mobile, String smscode,String invite_code, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("phone", mobile);
        params.put("code", smscode);
        params.put("pyqcode", invite_code);
        return IfOkNet.getInstance().post(Contact.LOGIN, params, callBack);
    }

    //第三方登录
    public static Call loginThird(String openId, String typeId, String nickname,String avatar,
                                  ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("openId", openId);
        params.put("typeId", typeId);
        params.put("nickName", nickname);
        params.put("avatar", avatar);
        return IfOkNet.getInstance().post(Contact.THIRD_LOGIN, params, callBack);
    }

    //第三方登录绑定手机号
    public static Call bindThird(String phone, String code,ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("phone", phone);
        params.put("code", code);
        return IfOkNet.getInstance().post(Contact.BIND_PHONE, params, callBack);
    }

    /**
     * 填写邀请码
     * @param pyqcode
     * @param callBack
     * @return
     */
    public static Call fillInvitation(String pyqcode,ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("pyqcode", pyqcode);
        return IfOkNet.getInstance().post(Contact.FILL_INVITATION, params, callBack);
    }

    //发送短息
    public static Call sendSms(String mobile, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("phone", mobile);
        return IfOkNet.getInstance().post(Contact.SEND_SMS, params, callBack);
    }
    //获取用户信息
    public static Call getUserInfo(String uid, ServiceCallBack<WrapUserBean> callBack) {
        Params params = new Params.Builder().json().build();
        params.put("uid", uid);
        return IfOkNet.getInstance().post(Contact.GETUSERINFO, params, callBack);
    }
    //获取用户信息
    public static Call modifyUserinfo(String uid,String username,String avatar, ServiceCallBack callBack) {
        Params params = new Params.Builder().json().build();
        params.put("uid", uid);
        params.put("username", username);
        params.put("avatar", avatar);
        return IfOkNet.getInstance().post(Contact.MODIFY_USERINFO, params, callBack);
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
     * @param lng
     * @param lat
     * @param callBack
     * @return
     */
    public static Call shakeWorker(int counts, double lng,double lat, ServiceCallBack<WrapWorkerBean> callBack) {
        Params params = new Params.Builder().json().build();
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
public static Call delAddress(String aid,String addressStatus,ServiceCallBack callBack){
    Params params = new Params.Builder().json().build();
    params.put("aid", aid);
    params.put("addressStatus", addressStatus);
    return IfOkNet.getInstance().post(Contact.ADDRESS_DELETE, params, callBack);
}

    /**
     * 生成订单
     * @param addressId
     * @param serviceTime
     * @param uid
     * @param payCount
     * @param orgId
     * @param workId
     * @param callBack
     * @return
     */
public static Call createOrder(String addressId,String serviceTime,String uid,
                               String payCount,String orgId,String workId,String serviceItemId,
                               double actualPay,
                               ServiceCallBack<WrapPrePayOrderBean> callBack){
    Params params = new Params.Builder().json().build();
    params.put("addressId", addressId);
    params.put("serviceTime", serviceTime);
    params.put("uid", uid);
    params.put("payCount", payCount);
    params.put("orgId", orgId);
    params.put("workId", workId);
    params.put("goodsId", serviceItemId);
    params.put("actualPay", actualPay);
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
public static Call cancelAndDelOrder(String orderid,int status,ServiceCallBack callBack){
    Params params = new Params.Builder().json().build();
    params.put("orderid",orderid);
    params.put("status",status);
    return IfOkNet.getInstance().post(Contact.UPDATE_ORDER, params, callBack);
}

    /**
     * 获取订单列表
     * @param uid
     * @param status
     * @param callBack
     * @return
     */
public static Call getOrderList(String uid,String status,int pageCurrent,int pageSize, ServiceCallBack<WrapOrderBean> callBack){
    Params params = new Params.Builder().json().build();
    params.put("uid",uid);
    params.put("status",status);
    params.put("pageCurrent",pageCurrent);
    params.put("pageSize",pageSize);
    return IfOkNet.getInstance().post(Contact.GET_ORDER_LIST, params, callBack);
}

    /**
     * 查看订单详情
     * @param orderid
     * @param callBack
     * @return
     */
public static Call getOrderDetails(String orderid, ServiceCallBack<WrapOrderDetailsBean> callBack){
    Params params = new Params.Builder().json().build();
    params.put("orderid",orderid);
    return IfOkNet.getInstance().post(Contact.GET_ORDER_DETAILS, params, callBack);
}

    /**
     *评论列表
     * @param callBack
     * @return
     */
    public static Call getMyCommentList(String uid,int pageCurrent,int pageSize, ServiceCallBack<WrapCommentBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("uid",uid);
        params.put("pageCurrent",pageCurrent);
        params.put("pageSize",pageSize);
        return IfOkNet.getInstance().post(Contact.GET_COMMENT, params, callBack);
    }

    /**
     * 发布评论
     * @param orderid
     * @param evaluation
     * @param score
     * @param callBack
     * @return
     */
    public static Call postComment(String orderid,String evaluation,String score, ServiceCallBack<WrapCommentBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("orderid",orderid);
        params.put("evaluation",evaluation);
        params.put("score",score);
        return IfOkNet.getInstance().post(Contact.POST_COMMENT, params, callBack);
    }
    /**
     * 发布需求
     * @param text
     * @param photo
     * @param addressname
     * @param voice
     * @param lng
     * @param atime
     * @param aPhone
     * @param uid
     * @param callBack
     * @return
     */
public static Call postRequirement(String text, String photo,
                                   String addressname, String voice,
                                   double lng, double lat,
                                   String serviceId, String atime,
                                   String aPhone, String uid,
                                   ServiceCallBack<PostResultBean> callBack){
    Params params = new Params.Builder().json().build();
    params.put("text",text);
    params.put("photo",photo);
    params.put("addressname",addressname);
    params.put("voice",voice);
    params.put("lng",lng);
    params.put("lat",lat);
    params.put("serviceId",serviceId);
    params.put("atime",atime);
    params.put("aPhone",aPhone);
    params.put("uid",uid);
    return IfOkNet.getInstance().post(Contact.POST_REQUIREMENT, params, callBack);//
}

    /**
     * 获取需求列表
     * @param uid
     * @param status
     * @param callBack
     * @return
     */
public static Call getRequirementList(String uid,String status,int pageCurrent,int pageSize, ServiceCallBack<WrapRequirementBean> callBack){

    Params params = new Params.Builder().json().build();
    params.put("uid",uid);
    params.put("status",status);
    params.put("pageCurrent",pageCurrent);
    params.put("pageSize",pageSize);
    return IfOkNet.getInstance().post(Contact.GET_REQUIREMENT, params, callBack);
}

    /**
     * 修改需求
     * @param id
     * @param status
     * @param callBack
     * @return
     */
public static Call updateRequirementList(String id,String status, ServiceCallBack<WrapRequirementBean> callBack){

    Params params = new Params.Builder().json().build();
    params.put("id",id);
    params.put("status",status);
    return IfOkNet.getInstance().post(Contact.UPDATE_REQUIREMENT, params, callBack);
}

    /**
     * @param callBack
     * @return
     */
public static Call applyWorker(String workerName,String idCard,String frontPhoto,
                               String negativePhoto,String sczmPhoto,String scfmPhoto,
                               String city,String cityCode,double lng,double lat,String phone,
                               String auditStatus,
                               ServiceCallBack<WrapRequirementBean> callBack){

    Params params = new Params.Builder().json().build();
    params.put("workerName",workerName);
    params.put("idCard",idCard);
    params.put("frontPhoto",frontPhoto);
    params.put("negativePhoto",negativePhoto);
    params.put("sczmPhoto",sczmPhoto);
    params.put("scfmPhoto",scfmPhoto);
    params.put("city",city);
    params.put("cityCode",cityCode);
    params.put("lng",lng);
    params.put("lat",lat);
    params.put("phone",phone);
    params.put("auditStatus",auditStatus);
    return IfOkNet.getInstance().post(Contact.APPLY_WORKER, params, callBack);
}

    /**
     * 获取七牛token
     * @param callBack
     * @return
     */
    public static Call getQiniuToken(ServiceCallBack<WrapQiNiuTokenBean> callBack){
        return IfOkNet.getInstance().post(Contact.GET_QINIUTOUKEN, null, callBack);
    }

    /**
     * 查询申请状态
     * @param callBack
     * @return
     */
    public static Call queryApplyStatus(ServiceCallBack<ApplyBean> callBack){
        return IfOkNet.getInstance().post(Contact.QUERY_APPLY_WORKER_STATUS, null, callBack);
    }

    /**
     * 检查更新
     * @param callBack
     * @return
     */
    public static Call checkUpdate(ServiceCallBack<WrapUpdateBean> callBack){
        return IfOkNet.getInstance().post(Contact.CHECK_UPDATE, null, callBack);
    }
    /**
     * 微信支付
     * @param orderid
     * @param actualPay
     * @param callBack
     * @return
     */
public static Call wxPay(String orderid, double actualPay , ServiceCallBack<WrapPrePayWeChatEntity> callBack){
    Params params = new Params.Builder().json().build();
    params.put("orderid",orderid);
    params.put("actualPay",actualPay);
    return IfOkNet.getInstance().post(Contact.WX_PAY, params, callBack);
}

    /**余额支付
     * @param orderid
     * @param actualPay
     * @param serviceItem
     * @param callBack
     * @return
     */
    public static Call walletPay(String orderid,double actualPay,String serviceItem,ServiceCallBack callBack){
        Params params = new Params.Builder().json().build();
        params.put("orderid",orderid);
        params.put("actualPay",actualPay);
        params.put("goodsName",serviceItem);
        return IfOkNet.getInstance().post(Contact.WALLET_PAY, params, callBack);
    }

    /**
     *根据银行卡号码获取银行卡归属地信息
     * @param bankId
     * @param callBack
     * @return
     */
public static Call getBankInfo(String bankId,ServiceCallBack callBack) {
    String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=" + bankId + "&cardBinCheck=true";
    return IfOk.getInstance().get(url, callBack);
}

    /**
     * 绑定银行卡
     * @param realName
     * @param idCardNo
     * @param bankCardNo
     * @param bankCode
     * @param phone
     * @param icon
     * @param callBack
     * @return
     */
public static Call bindBankCard(String realName,String idCardNo,String bankCardNo,String bankCode,
                                String phone,String icon,ServiceCallBack callBack){
    Params params = new Params.Builder().json().build();
    params.put("realName",realName);
    params.put("idCardNo",idCardNo);
    params.put("bankCardNo",bankCardNo);
    params.put("bankCode",bankCode);
    params.put("phone",phone);
    params.put("icon",icon);
    return IfOkNet.getInstance().post(Contact.BIND_CARD, params, callBack);
}

    /**
     * 获取银行卡列表
     * @param callBack
     * @return
     */
    public static Call getBindBankCard(ServiceCallBack<WrapBankBean> callBack){
    return IfOkNet.getInstance().post(Contact.GET_BANK_LIST, null, callBack);
    }

    /**
     * 申请提现
     * @param callBack
     * @return
     */
    public static Call applyWithdrawal(String bankCardNo,String money,ServiceCallBack callBack){
        Params params = new Params.Builder().json().build();
        params.put("bankCardNo",bankCardNo);
        params.put("money",money);
    return IfOkNet.getInstance().post(Contact.APPLY_WITHDRAWAL, params, callBack);
    }

    /**
     * 钱包
     * @param offset
     * @param pageSize
     * @param callBack
     * @return
     */
    public static Call queryWallet(int offset,int pageSize,ServiceCallBack<WrapWalletBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("offset",offset);
        params.put("pageSize",pageSize);
    return IfOkNet.getInstance().post(Contact.GET_WALLET_INFO, params, callBack);

    }
    /**
     * 提现
     * @param callBack
     * @return
     */
    public static Call gotoWithdrawal(ServiceCallBack<WrapCashBean> callBack){
    return IfOkNet.getInstance().post(Contact.WITHDRAWAL, null, callBack);
    }

    /**
     * 提现记录
     * @param callBack
     * @return
     */
    public static Call getWithdrawalRecord(ServiceCallBack<WrapPaymentDetailsBean> callBack){
    return IfOkNet.getInstance().post(Contact.GET_WITHDRAWAL_RECORD, null, callBack);
    }

    /**
     * 获取积分消费记录
     * @param callBack
     * @return
     */
    public static Call getRewardpointList(int offset,int pageSize,ServiceCallBack<WrapRewardpointBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("offset",offset);
        params.put("pageSize",pageSize);
        return IfOkNet.getInstance().post(Contact.REWARDPOINT_LIST, params, callBack);
    }

    /**
     * 积分支付
     * @param orderid
     * @param actualPay
     * @param serviceItem
     * @param callBack
     * @return
     */
    public static Call rewardpointPay(String orderid,double actualPay,String serviceItem,ServiceCallBack callBack){
        Params params = new Params.Builder().json().build();
        params.put("orderid",orderid);
        params.put("actualPay",actualPay);
        params.put("serviceItem",serviceItem);
        return IfOkNet.getInstance().post(Contact.REWARDPOINT_PAY, params, callBack);
    }
}

