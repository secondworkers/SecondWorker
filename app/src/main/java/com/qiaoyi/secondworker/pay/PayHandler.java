package com.qiaoyi.secondworker.pay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

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
    public static void onRequset(Context context,String order_id,double price) {
        ApiUserService.wxPay(order_id, price, new ServiceCallBack<PrePayWeChatEntity>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<PrePayWeChatEntity> payload) {
                PrePayWeChatEntity payload1 = payload.body();//判断是否是零元订单
                if (!TextUtils.isEmpty(payload1.prepayid)){
                    WeChatPay(payload1,context);
                }else {
                    context.startActivity(new Intent(context,PostSuccessActivity.class));
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
        PayReq req = new PayReq();

        req.appId = data.appid;

        req.partnerId = data.partnerid;

        req.prepayId = data.prepayid;

        req.packageValue = data.packages;

        req.nonceStr = data.noncestr;

        req.timeStamp = data.timestamp;

        req.sign = data.sign;

        api.registerApp(data.appid);

        //发起请求
        api.sendReq(req);
    }
    private static boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }
}
