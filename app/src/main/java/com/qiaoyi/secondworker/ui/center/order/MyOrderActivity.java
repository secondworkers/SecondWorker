package com.qiaoyi.secondworker.ui.center.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

/**
 * Created on 2019/5/7
 * 我的订单
 * @author Spirit
 */

public class MyOrderActivity extends BaseActivity {
    private TextView tv_title_txt;
    private RadioButton rb_all;
    private RadioButton rb_waiting_pay;
    private RadioButton rb_waiting_service;
    private RadioButton rb_waiting_confirm;
    private RadioButton rb_waiting_comment;
    private RadioGroup rg_base;
    private RecyclerView rv_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_my_orders);
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        rb_all = (RadioButton) findViewById(R.id.rb_all);
        rb_waiting_pay = (RadioButton) findViewById(R.id.rb_waiting_pay);
        rb_waiting_service = (RadioButton) findViewById(R.id.rb_waiting_service);
        rb_waiting_confirm = (RadioButton) findViewById(R.id.rb_waiting_confirm);
        rb_waiting_comment = (RadioButton) findViewById(R.id.rb_waiting_comment);
        rg_base = (RadioGroup) findViewById(R.id.rg_base);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
    }
    void initData(){
        tv_title_txt.setText("我的订单");
    }
}
