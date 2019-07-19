package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/5/9
 *
 * @author Spirit
 */

public class OrderBean {

    /**
     * orderid : 
     * uid : 
     * paytime : 2019-05-05 11:08:55.0
     * creatime : 2019-05-05 08:08:20.0
     * finishTime : 
     * workId : 1
     * serviceTime : 
     * membership : 
     * actualPay : 9.0
     * addressId : 9
     * status : 3
     * cancelReasons : 
     * score : 10.0
     * evaluation : null
     * rate : null
     * userName : 
     * serviceLength : 0
     * likeButton : 0
     * et : null
     * merchantReply : null
     * replyTime : 
     * orderNum : 051044015357204982
     * orgId : 
     * statuss : dfw
     * serviceItem : 空气净化
     * screenName : 孙玉龙
     * etStr : null
     * addressname : 泰山国际大厦
     * addressDetailName : 九楼獒牙众创空间A-15
     * addressphone : 18654952019
     * price : 25
     * icon : https://up.enterdesk.com/edpic_source/c5/13/4a/c5134a615dcd5b138746694f21128964.jpg
     * workerName : 秒工2
     * unit : 元/小时
     * totalPrice : 50
     */

    public String orderid;
    public String uid;
    public String paytime;
    public String creatime;
    public String finishTime;
    public int workId;
    public String serviceTime;
    public String membership;
    public double actualPay;
    public int addressId;
    public int status;
    public String cancelReasons;
    public double score;
    public Object evaluation;
    public Object rate;
    public String userName;
    public int serviceLength;
    public int likeButton;
    public Object et;
    public Object merchantReply;
    public String replyTime;
    public String orderNum;
    public String orgId;
    public String statuss;
    public String goodsName;
    public String goodsPhoto;
    public String screenName;
    public Object etStr;
    public String addressname;
    public String addressDetailName;
    public String addressphone;
    public double price;
    public String icon;
    public String workerName;
    public String unit;
    public String payCount;
    public String orgName;
    public int totalPrice;
    public long ctime;
    public long systemTime;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
