package com.qiaoyi.secondworker.ui.center.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.qiaoyi.secondworker.ui.center.wallet.RechargeActivity;
import com.qiaoyi.secondworker.ui.shake.activity.PostSuccessActivity;
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
    private CheckBox cb_alipay,cb_integral_pay;
    private TextView tv_total_price1;
    private TextView tv_goto_pay,tv_balance,tv_integral;
    private CountDownTimer countDownTimer;
    private long timStamp = 900000;//剩余支付时间
    private String order_id,service_name;
    private long createTime;
    private long currentTime;
    private double t_price;
    private double balance;
    private double rewardPoints;

    public static void startPrePayActivity(Activity activity,String order_id,String service_name,double t_price){
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
        requestTime();
        initData();
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
                balance = result.banlance;
                rewardPoints = result.rewardPoints;
                tv_balance.setText(Html.fromHtml("<font color='#212121'>"
                        + "余额："+String.valueOf(balance)
                        + "</font><font color='#00A2FF'> "
                        + "充值"
                        + "</font>"));
                tv_integral.setText("积分："+String.valueOf(rewardPoints));
                timStamp = createTime + 900000 - currentTime;
                if (timStamp >= 1000){
                    countDownTimer(timStamp);
                    tv_goto_pay.setClickable(true);
                    tv_goto_pay.setBackground(getResources().getDrawable(R.drawable.shape_gradient_little_no_coners_blue));
                } else {
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
        cb_integral_pay = (CheckBox) findViewById(R.id.iv_integral_pay);
        cb_alipay = (CheckBox) findViewById(R.id.iv_alipay);
        tv_total_price1 = (TextView) findViewById(R.id.tv_total_price1);
        tv_goto_pay = (TextView) findViewById(R.id.tv_goto_pay);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_integral = (TextView) findViewById(R.id.tv_integral);
        tv_title_txt.setText("支付订单");
        view_back.setOnClickListener(this);
        tv_goto_pay.setOnClickListener(this);
        tv_balance.setOnClickListener(this);
    }
    private void initData() {
        //支付方式
        tv_service_type.setText(Html.fromHtml("<font color='#212121'>"
                + "订单类型："
                + "</font><font color='#666666'> "
                + service_name
                + "</font>"));
        tv_order_price.setText(Html.fromHtml("<font color='#212121'>"
                + "订单金额："
                + "</font><font color='#FF0000'> "
                + String.valueOf(t_price)
                + "元 </font>"));
        tv_total_price1.setText(String.valueOf(t_price)+"元");
        cb_wechat_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_alipay.setChecked(false);
                    cb_integral_pay.setChecked(false);
                }
            }
        });//
        cb_alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_wechat_pay.setChecked(false);
                    cb_integral_pay.setChecked(false);
                }
            }
        });
        cb_integral_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_wechat_pay.setChecked(false);
                    cb_alipay.setChecked(false);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_balance:
                Intent intent = new Intent(this, RechargeActivity.class);
                intent.putExtra("balance",String.valueOf(balance));
                startActivity(intent);
                break;
            case R.id.tv_goto_pay:
                //根据选择的支付方式 调起支付
                if (cb_wechat_pay.isChecked()){
                    if (t_price > 0){
                        tv_goto_pay.setClickable(false);
                        PayHandler.onRequest(this,order_id,t_price,0.0,service_name,1,"");
                    }else {
                        PostSuccessActivity.startSuccessActivity(this,String.valueOf(t_price),service_name,"pay");
                        finish();
                    }
                }else if (cb_alipay.isChecked()){
                    if ((t_price - balance)<=0){
                        ApiUserService.walletPay(order_id, t_price, service_name, new ServiceCallBack() {
                            @Override
                            public void failed(String code, String errorInfo, String source) {
                                ToastUtils.showShort(errorInfo);
                            }

                            @Override
                            public void success(RespBean resp, Response payload) {
                                PostSuccessActivity.startSuccessActivity(PrePayActivity.this,String.valueOf(t_price),service_name,"pay");
                                finish();
                            }
                        });
                    }else {
                        ToastUtils.showShort("余额不足请选择其他支付方式");
                        return;
                    }

                } else if (cb_integral_pay.isChecked()){
                    if ((t_price - rewardPoints)<=0) {
                        ApiUserService.rewardpointPay(order_id, t_price, service_name, new ServiceCallBack() {
                            @Override
                            public void failed(String code, String errorInfo, String source) {
                                ToastUtils.showShort(errorInfo);
                            }

                            @Override
                            public void success(RespBean resp, Response payload) {
                                PostSuccessActivity.startSuccessActivity(PrePayActivity.this, String.valueOf(t_price), service_name, "pay");
                                finish();
                            }
                        });
                    }else {
                        ToastUtils.showShort("积分不足请选择其他支付方式");
                        return;
                    }
                }
                break;
        }
    }
    private void cancelOrderById() {
        ApiUserService.cancelAndDelOrder(order_id,5,new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
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

    @Override
    protected void onResume() {
        super.onResume();
        tv_goto_pay.setClickable(true);
    }
}
