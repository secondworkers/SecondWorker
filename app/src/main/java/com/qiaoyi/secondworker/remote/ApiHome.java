package com.qiaoyi.secondworker.remote;

import com.qiaoyi.secondworker.bean.WrapMapWorkerBean;
import com.qiaoyi.secondworker.bean.WrapSearchServiceBean;
import com.qiaoyi.secondworker.bean.WrapServiceBean;
import com.qiaoyi.secondworker.bean.WrapServiceDetailsBean;
import com.qiaoyi.secondworker.bean.WrapServiceItemBean;
import com.qiaoyi.secondworker.bean.WrapServiceItemByIdBean;
import com.qiaoyi.secondworker.bean.WrapWorkerBean;
import com.qiaoyi.secondworker.bean.WrapWorkerDetailsBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.IfOkNet;
import com.qiaoyi.secondworker.net.ServiceCallBack;

import cn.isif.ifok.Params;
import okhttp3.Call;

/**
 * Created on 2019/5/9
 *
 * @author Spirit
 */

public class ApiHome {
    /**
     * 获取 map title
     * @param callBack
     * @return
     */
    public static Call getServiceType(ServiceCallBack<WrapServiceBean> callBack){
        Params params = new Params.Builder().json().build();
        return IfOkNet.getInstance().post(Contact.SERVICE_TYPE, params, callBack);
    }

    /**
     * 服务子项目
     * @param callBack
     * @return
     */
    public static Call getServiceItem(ServiceCallBack<WrapServiceItemBean> callBack){
        Params params = new Params.Builder().json().build();
        return IfOkNet.getInstance().post(Contact.SERVICE_ITEM, params, callBack);
    }

    /**
     * 服务详情
     * @param serviceItemId
     * @param callBack
     * @return
     */
 public static Call getServiceDetail(String serviceItemId,ServiceCallBack<WrapServiceDetailsBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("serviceItemId",serviceItemId);
        return IfOkNet.getInstance().post(Contact.GET_SERVICE_DETAIL, params, callBack);
    }

    /**
     * 筛选条件
     * @param serviceTypeId
     * @param callBack
     * @return
     */
    public static Call getServiceItemById(String serviceTypeId,ServiceCallBack<WrapServiceItemByIdBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("serviceTypeId",serviceTypeId);
        return IfOkNet.getInstance().post(Contact.GET_SERVICE_ITEM, params, callBack);
    }

    /**
     *搜索展示列表
     * @param serviceItem
     * @param lng
     * @param lat
     * @param order
     * @param serviceRange
     * @param serviceItemId
     * @param callBack
     * @return
     */
 public static Call searchSearch(String serviceItem,double lng,double lat,
                                 String order,String serviceRange,
                                 String serviceItemId,ServiceCallBack<WrapSearchServiceBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("serviceItem",serviceItem);
        params.put("lng",lng);
        params.put("lat",lat);
        params.put("order",order);
        params.put("serviceRange",serviceRange);
        params.put("serviceItemId",serviceItemId);
        return IfOkNet.getInstance().post(Contact.SEARCH_QUERY_LIST, params, callBack);
    }

    /**
     * 地图上显示工作人员
     * @param serviceTypeId
     * @param
     * @param
     * @param callBack
     * @return
     */
    public static Call getMapWorkers(int serviceTypeId,
                                     double leftTopLng,double leftTopLat,
                                     double rightBottomLng,double rightBottomLat,
                                     ServiceCallBack<WrapMapWorkerBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("serviceTypeId",serviceTypeId);
        params.put("leftTopLng",leftTopLng);
        params.put("leftTopLat",leftTopLat);
        params.put("rightBottomLng",rightBottomLng);
        params.put("rightBottomLat",rightBottomLat);
        return IfOkNet.getInstance().post(Contact.GET_MAP_WORKERS, params, callBack);
    }

    /**
     *
     * @param workerId
     * @param lng
     * @param lat
     * @param callBack
     * @return
     */
    public static Call getWorkerDetail(String workerId,double lng,double lat,ServiceCallBack<WrapWorkerDetailsBean> callBack){
        Params params = new Params.Builder().json().build();
        params.put("workerId",workerId);
        params.put("lng",lng);
        params.put("lat",lat);
        return IfOkNet.getInstance().post(Contact.GET_WORKER_DETAIL, params, callBack);
    }
}
