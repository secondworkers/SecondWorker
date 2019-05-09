package com.qiaoyi.secondworker.pay;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PrePayWeChatEntity implements Serializable{
    /*
    * {
  "appId": "",
  "partnerId": "",
  "prepayId": "",
  "sign": "",
  "nonceStr" : "",
  "timeStamp": ""
}
    * */
    public int code;
    public String appid;
    public String partnerid;
    public String prepayid;
    public String sign;
    public String noncestr;
    public String timestamp;
    @SerializedName("package")
    public String packages;
}
