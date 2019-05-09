package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/5/8
 *
 * @author Spirit
 */

public class OrderConfirmEvent {
    private String address_title_msg;
    private String address_name_phone;
    private String address_id;

    public OrderConfirmEvent(String address_title_msg, String address_name_phone, String address_id) {
        this.address_title_msg = address_title_msg;
        this.address_name_phone = address_name_phone;
        this.address_id = address_id;
    }

    public String getAddress_title_msg() {
        return address_title_msg;
    }

    public void setAddress_title_msg(String address_title_msg) {
        this.address_title_msg = address_title_msg;
    }

    public String getAddress_name_phone() {
        return address_name_phone;
    }

    public void setAddress_name_phone(String address_name_phone) {
        this.address_name_phone = address_name_phone;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }
}
