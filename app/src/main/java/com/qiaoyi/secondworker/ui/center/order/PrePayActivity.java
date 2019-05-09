package com.qiaoyi.secondworker.ui.center.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.PrePayOrderBean;
import com.qiaoyi.secondworker.bean.WrapPrePayOrderBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.pay.PayHandler;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/27
 *  支付订单
 * @author Spirit
 */

public class PrePayActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_count_time;
    private TextView tv_service_type;
    private TextView tv_order_price;
    private CheckBox cb_wechat_pay;
    private CheckBox cb_alipay;
    private TextView tv_total_price1;
    private TextView tv_goto_pay;
    private CountDownTimer countDownTimer;
    private long timStamp = 900000;//剩余支付时间
    private String order_id,service_name;
    private long createTime;
    private long currentTime;
    private double t_price;

    public static void StartPrePayActivity(Activity activity,String order_id,String service_name,double t_price){
        Intent intent = new Intent(activity, PrePayActivity.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("service_name",service_name);
        intent.putExtra("t_price",t_price);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_prepay);
        initView();
        initData();
        requestTime();
    }

    private void requestTime() {
        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        service_name = intent.getStringExtra("service_name");
        t_price = intent.getDoubleExtra("t_price",0.0);
        ApiUserService.getOrderInfo(order_id, new ServiceCallBack<WrapPrePayOrderBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapPrePayOrderBean> payload) {
                WrapPrePayOrderBean body = payload.body();
                PrePayOrderBean result = body.result;
                createTime = result.createTime;
                currentTime = result.currentTime;
                timStamp = createTime + 900000 - currentTime;
                if (timStamp >= 1000){
                    countDownTimer(timStamp);
                    tv_goto_pay.setClickable(true);
                    tv_goto_pay.setBackground(getResources().getDrawable(R.drawable.shape_gradient_little_no_coners_blue));
                }else {
                    cancelOrderById();
                }
            }
        });
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_count_time = (TextView) findViewById(R.id.tv_count_time);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        cb_wechat_pay = (CheckBox) findViewById(R.id.iv_wechat_pay);
        cb_alipay = (CheckBox) findViewById(R.id.iv_alipay);
        tv_total_price1 = (TextView) findViewById(R.id.tv_total_price1);
        tv_goto_pay = (TextView) findViewById(R.id.tv_goto_pay);
        tv_title_txt.setText("支付订单");
        view_back.setOnClickListener(this);
        tv_goto_pay.setOnClickListener(this);
    }
    private void initData() {
        //支付方式

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_goto_pay:
                //根据选择的支付方式 调起支付
                if (cb_wechat_pay.isChecked()){
                    PayHandler.onRequset(this,order_id,t_price);
                }else if (cb_alipay.isChecked()){
                    //alipay
                }
                break;
        }
    }
    private void cancelOrderById() {
        ApiUserService.cancelAndDelOrder(order_id,"cancel",new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response payload) {
                ALog.e("订单超时，取消");
                tv_count_time.setText("支付超时,订单取消，请重新下单！");
                tv_goto_pay.setClickable(false);
                tv_goto_pay.setBackgroundColor(getResources().getColor(R.color.text_grey));
            }
        });
    }

    void countDownTimer(long timStamp){
        countDownTimer = new CountDownTimer(timStamp, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_count_time.setText(VwUtils.longToTimeStr(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                //请求取消订单接口
                cancelOrderById();
            }
        };
        countDownTimer.start();
    }
}
