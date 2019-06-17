package com.qiaoyi.secondworker.bean;

import java.io.Serializable;

/**
 * Created on 2019/4/19
 *
 * @author Spirit
 */

public class UserBean implements Serializable {

    /**
     * username : “”
     * password : “”
     * name : “”
     * sex : “”
     * idcard : “”
     * role : “”
     * birthday : “”l
     * avatar : 
     * openid : 2
     * adddate : “”
     * status : 0
     * deviceId : “”
     * nickName : “”
     * typeId : 1
     * count : 0
     * token : “”
     */

    public String uid;
    public String username;
    public String phone;
    public String password;
    public String name;
    public String sex;
    public String idcard;
    public String role;
    public String birthday;
    public String avatar;
    public String openid;
    public String adddate;
    public String status;
    public String deviceId;
    public String nickName;
    public String typeId;
    public int count;
    public int auditStatus;
    public String token;
    public String createTime;
    public double balance;
    public double rewardpoints;

}
