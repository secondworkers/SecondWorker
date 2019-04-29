package com.qiaoyi.secondworker.ui.center.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

/**
 * Created on 2019/4/27
 *  确认订单
 * @author Spirit
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_address;
    private TextView tv_username_phone;
    private TextView tv_service_time;
    private TextView tv_service_name;
    private TextView tv_single_price;
    private TextView tv_service_time_long;
    private TextView tv_vip_discount;
    private TextView tv_total_price;
    private TextView tv_total_price1;
    private TextView tv_goto_pay;
    private RelativeLayout rl_service_location;
    private RelativeLayout rl_service_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_confirm_order);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_username_phone = (TextView) findViewById(R.id.tv_username_phone);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        tv_service_name = (TextView) findViewById(R.id.tv_service_name);
        tv_single_price = (TextView) findViewById(R.id.tv_single_price);
        tv_service_time_long = (TextView) findViewById(R.id.tv_service_time_long);
        tv_vip_discount = (TextView) findViewById(R.id.tv_vip_discount);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_total_price1 = (TextView) findViewById(R.id.tv_total_price1);
        tv_goto_pay = (TextView) findViewById(R.id.tv_goto_pay);

        view_back.setOnClickListener(this);
        tv_goto_pay.setOnClickListener(this);
        rl_service_location = (RelativeLayout) findViewById(R.id.rl_service_location);
        rl_service_location.setOnClickListener(this);
        rl_service_time = (RelativeLayout) findViewById(R.id.rl_service_time);
        rl_service_time.setOnClickListener(this);

        tv_title_txt.setText("确认订单");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_goto_pay:
                Intent intent = new Intent(this, PrePayActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_service_location:
                startActivityForResult(new Intent(this,GetAddressActivity.class),2001);
                break;
            case R.id.rl_service_time:
                startActivityForResult(new Intent(this,GetAddressActivity.class),2002);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK){
           if (requestCode == 2001){
               //
           }else if (requestCode == 2002){

           }
        }
    }
}
