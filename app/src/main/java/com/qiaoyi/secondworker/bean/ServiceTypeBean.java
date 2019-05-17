package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/4/26
 *
 * @author Spirit
 */

public class ServiceTypeBean {

    /**
     * serviceType : 保洁
     * introductiondetail : 保洁简介内容
     * createtime : 2019-04-24 17:22:44.0
     * createUserId :
     * id : 1
     */

    public String serviceType;
    public String introductiondetail;
    public String createtime;
    public String createUserId;
    public String serviceTypeId;
    public String id;
    public String icon;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
