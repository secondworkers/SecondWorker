package com.qiaoyi.secondworker.pay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.qiaoyi.secondworker.bean.WrapPrePayWeChatEntity;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.shake.activity.PostSuccessActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.isif.alibs.utils.ToastUtils;

public class PayHandler {
    /**
     * @param context
     * @param order_id
     * @param price
     * @param rewardPoint
     * @param service_name
     * @param type 1(支付) 2（充值）
     */
    public static void onRequest(Activity context, String order_id, double price ,double rewardPoint,String service_name,int type,String pyqCode) {
        ApiUserService.wxPay(order_id,price,rewardPoint,type,pyqCode, new ServiceCallBack<WrapPrePayWeChatEntity>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapPrePayWeChatEntity> payload) {
                PrePayWeChatEntity payload1 = payload.body().result;//判断是否是零元订单
                if (!TextUtils.isEmpty(payload1.prepayid)){
                    WeChatPay(payload1,context);
                }else {
                    PostSuccessActivity.startSuccessActivity(context,String.valueOf(price),service_name,"pay");
                }
            }
        });
    }

    //PrePayWeChatEntity 服务器返回给我们微信支付的参数

    private static void WeChatPay(PrePayWeChatEntity data,Context context) {

        if (null == data) {
            //判断是否为空。丢一个toast，给个提示。比如服务器异常，错误啥的
            ToastUtils.showShort("服务器异常。。");
            return;
        }

        IWXAPI api = WXAPIFactory.createWXAPI(context, data.appid,false);

        if (isWXAppInstalledAndSupported(api)){

        }else {
            ToastUtils.showShort("未检测到微信或您当前微信版本不支持支付功能");
        }


        //data  根据服务器返回的json数据创建的实体类对象
        PayReq req = new PayReq();//支付返回数据格式要和之前的统一

        req.appId = data.appid;

        req.partnerId = data.partnerid;

        req.prepayId = data.prepayid;

        req.packageValue = data.packages;

        req.nonceStr = data.noncestr;

        req.timeStamp = data.timestamp;

        req.sign = data.sign;

//        api.registerApp(data.appid);

        //发起请求
        api.sendReq(req);
    }
    private static boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }
}
