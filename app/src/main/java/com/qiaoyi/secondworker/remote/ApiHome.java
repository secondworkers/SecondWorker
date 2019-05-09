package com.qiaoyi.secondworker.remote;

import com.qiaoyi.secondworker.bean.WrapServiceBean;
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
     * 获取map title
     * @param callBack
     * @return
     */
    public static Call getServiceType(ServiceCallBack<WrapServiceBean> callBack){
        Params params = new Params.Builder().json().build();
        return IfOkNet.getInstance().post(Contact.SERVICE_TYPE, params, callBack);
    }
}
