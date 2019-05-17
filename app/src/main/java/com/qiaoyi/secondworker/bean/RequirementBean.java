package com.qiaoyi.secondworker.bean;

public class RequirementBean {

    /**
     * id : 
     * uid : 
     * text : 我需要一个保洁
     * photo : 
     * addressname : 
     * voice : 
     * serviceId : 8
     * lng : 0.0
     * lat : 0.0
     * atime : 2019-05-06 16:00:00
     * aPhone : 
     * status : 0
     * releaseTime : 2019-05-09 05:51
     */

    public String id;
    public String uid;
    public String text;
    public String photo;
    public String addressname;
    public String voice;
    public String serviceId;
    public double lng;
    public double lat;
    public String atime;
    public String aPhone;
    public String status;
    public String releaseTime;
    public String serviceType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
