package com.qiaoyi.secondworker.bean;

import java.util.List;

/**
 * Created on 2019/5/11
 *
 * @author Spirit
 */

public class ServiceItemBean {

    /**
     * serviceType : 保洁
     * serviceItemList : [{"id":"2","serviceItem":"擦玻璃","price":"40.00","introduction":null,"serviceTenet":null,"serviceRange":0,"serviceTypeId":"1","number":"2","unit":"元","serviceType":"保洁","lowerPrice":0,"heigherPrice":0,"order":null,"type":null,"fuwushang":null,"username":null,"score":0,"evaluation":null,"otherExplain":null,"introductiondetail":null,"counts":0,"lng":0,"lat":0}]
     * serviceTypeId : 1
     */
    public String serviceType;
    public String serviceTypeId;
    public List<ServiceItemListBean> serviceItemList;
}
