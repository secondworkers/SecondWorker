package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/6/5
 *
 * @author Spirit
 */

public class PaymentDetailsBean {

    /**
     * width : 0
     * pageSize : 20
     * selectPageSize : 5,10,20,50,100
     * pageCurrent : 1
     * total : 0
     * showPagenum : 0
     * offset : 0
     * id : 42c9cd4abf4b4ec2bcb25a2dfc740cd3
     * balance : 7993
     * type : 1     1:消费  3：提现 | 2：收益 4：充值
     * money : 20
     * auditStatus :  
     * itemName : 日常保洁
     * createTime : 2019-05-31
     * uid : 62c9cd4abf4b4ec2bcb25a2dfc740cd3
     * orderId : “”
     * bankCardNo : “”
     */

    public int width;
    public int pageSize;
    public String selectPageSize;
    public int pageCurrent;
    public int total;
    public int showPagenum;
    public int offset;
    public String id;
    public double balance;
    public int type;
    public double money;
    public String auditStatus;
    public String itemName;
    public String createTime;
    public String uid;
    public String orderId;
    public String bankCardNo;
}
