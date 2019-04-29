package com.qiaoyi.secondworker.bean;

/**
 * Created on 2019/4/28
 *
 * @author Spirit
 */

public class PositionEntity {
    public double latitue;
    public double longitude;
    public String address;
    public String city;
    public PositionEntity() {}
    public PositionEntity(double latitude, double longtitude, String address, String city) {
        this.latitue = latitude;
        this.longitude = longtitude;
        this.address = address;
        this.city = city;
    }

    @Override
    public String toString() {
        return "PositionEntity{" +
                "latitue=" + latitue +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
