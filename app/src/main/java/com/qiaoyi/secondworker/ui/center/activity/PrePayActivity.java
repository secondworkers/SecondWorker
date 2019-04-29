package com.qiaoyi.secondworker.ui.center.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

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
    private CheckBox iv_wechat_pay;
    private CheckBox iv_alipay;
    private TextView tv_total_price1;
    private TextView tv_goto_pay;
    private CountDownTimer countDownTimer;
    private long timStamp = 900000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_prepay);
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_count_time = (TextView) findViewById(R.id.tv_count_time);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        iv_wechat_pay = (CheckBox) findViewById(R.id.iv_wechat_pay);
        iv_alipay = (CheckBox) findViewById(R.id.iv_alipay);
        tv_total_price1 = (TextView) findViewById(R.id.tv_total_price1);
        tv_goto_pay = (TextView) findViewById(R.id.tv_goto_pay);
        tv_title_txt.setText("支付订单");
        view_back.setOnClickListener(this);
        tv_goto_pay.setOnClickListener(this);
    }
    private void initData() {
        countDownTimer = new CountDownTimer(timStamp, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_count_time.setText(formatLongToTimeStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                //请求取消订单接口
            }
        };
        countDownTimer.start();
    }
    public String formatLongToTimeStr(Long l) {
        long day = l / (1000 * 24 * 60 * 60); //单位天
        long hour = (l - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
        long minute = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
        long second = (l - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒

        String strtime = minute+"分"+second+"秒";
        return strtime;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_goto_pay:
                //根据选择的支付方式 调起支付
                break;
        }
    }
}
