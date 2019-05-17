package com.qiaoyi.secondworker.ui.center.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OrderBean;
import com.qiaoyi.secondworker.bean.WrapOrderBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.Contacts;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.ItemDecoration.MyItemDecoration;
import com.qiaoyi.secondworker.ui.center.adapter.OrderListAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/7
 * 我的订单
 *
 * @author Spirit
 */

public class MyOrderActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RadioButton rb_all;
    private RadioButton rb_waiting_pay;
    private RadioButton rb_waiting_service;
    private RadioButton rb_waiting_confirm;
    private RadioButton rb_waiting_comment;
    private RadioGroup rg_base;
    private RecyclerView rv_list;
    private OrderListAdapter listAdapter;
    private String status;
    private RelativeLayout view_back;
    private List<OrderBean> result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_my_orders);
        status = getIntent().getStringExtra("status");
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
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_back.setOnClickListener(this);
    }

    void initData() {
        tv_title_txt.setText("我的订单");
       switch (status){
           case Contacts.ALL_ORDERS:
               rb_all.setChecked(true);
               requestData(Contacts.ALL_ORDERS);
               break;
           case Contacts.WAITING_PAY:
               rb_waiting_pay.setChecked(true);
               requestData(Contacts.WAITING_PAY);
               break;
           case Contacts.WAITING_SERVICE:
               rb_waiting_service.setChecked(true);
               requestData(Contacts.WAITING_SERVICE);
               break;
           case Contacts.WAITING_CONFIRM:
               rb_waiting_confirm.setChecked(true);
               requestData(Contacts.WAITING_CONFIRM);
               break;
           case Contacts.WAITING_COMMENT:
               rb_waiting_comment.setChecked(true);
               requestData(Contacts.WAITING_COMMENT);
               break;
       }
        rg_base.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_all:
                        requestData(Contacts.ALL_ORDERS);
                        break;
                    case R.id.rb_waiting_pay:
                        requestData(Contacts.WAITING_PAY);
                        break;
                    case R.id.rb_waiting_service:
                        requestData(Contacts.WAITING_SERVICE);
                        break;
                    case R.id.rb_waiting_confirm:
                        requestData(Contacts.WAITING_CONFIRM);
                        break;
                    case R.id.rb_waiting_comment:
                        requestData(Contacts.WAITING_COMMENT);
                        break;
                }
            }
        });
        ((SimpleItemAnimator) rv_list.getItemAnimator()).setSupportsChangeAnimations(false);
        int itemDecorationCount = rv_list.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            rv_list.addItemDecoration(new MyItemDecoration(0, 0, 0, 0));
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listAdapter = new OrderListAdapter(R.layout.item_order, this);

        rv_list.setAdapter(listAdapter);
        rv_list.setLayoutManager(manager);
    }

    private void requestData(String requestStatus) {
        ApiUserService.getOrderList(AccountHandler.getUserId(), requestStatus, new ServiceCallBack<WrapOrderBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapOrderBean> payload) {
                result = payload.body().result;
                listAdapter.setNewData(result);
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
