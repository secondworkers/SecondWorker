package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/4/28
 *
 * @author Spirit
 */

public class LocationBean {
    private double lat;
    private double lng;
    private String address_title;
    private String address_msg;

    public LocationBean(double lat, double lng,String address_title,String address_msg) {
        this.lat = lat;
        this.lng = lng;
        this.address_title = address_title;
        this.address_msg = address_msg;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress_title() {
        return address_title;
    }

    public void setAddress_title(String address_title) {
        this.address_title = address_title;
    }

    public String getAddress_msg() {
        return address_msg;
    }

    public void setAddress_msg(String address_msg) {
        this.address_msg = address_msg;
    }
}
