package com.qiaoyi.secondworker.ui.center.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.qiaoyi.secondworker.local.Config;
import com.qiaoyi.secondworker.net.Contact;
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
import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.BLUE;

/**
 * Created on 2019/5/7
 * 我的订单
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
    private SwipeRefreshLayout refreshLayout;
    private MultiStateLayout multiStateLayout;
    int currentPage = 1;
    private String requestStatus = Contacts.ALL_ORDERS;

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
        refreshLayout = findViewById(R.id.srl_wrap);
        multiStateLayout = findViewById(R.id.msl_layout);
        view_back.setOnClickListener(this);
    }

    void initData() {
        tv_title_txt.setText("我的订单");

        rg_base.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_all:
                        requestData(Contacts.ALL_ORDERS,true);
                        requestStatus = Contacts.ALL_ORDERS;
                        break;
                    case R.id.rb_waiting_pay:
                        requestData(Contacts.WAITING_PAY,true);
                        requestStatus = Contacts.WAITING_PAY;
                        break;
                    case R.id.rb_waiting_service:
                        requestData(Contacts.WAITING_SERVICE,true);
                        requestStatus = Contacts.WAITING_SERVICE;
                        break;
                    case R.id.rb_waiting_confirm:
                        requestData(Contacts.WAITING_CONFIRM,true);
                        requestStatus = Contacts.WAITING_CONFIRM;
                        break;
                    case R.id.rb_waiting_comment:
                        requestData(Contacts.WAITING_COMMENT,true);
                        requestStatus = Contacts.WAITING_COMMENT;
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
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(requestStatus,true);
            }
        });
        listAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestData(requestStatus,false);
            }
        }, rv_list);
        multiStateLayout.setStateListener(new MultiStateLayout.StateListener() {
            @Override
            public void onStateChanged(int viewState) {
            }
            @Override
            public void onStateInflated(int viewState, @NonNull View view) {
                if (viewState == MultiStateLayout.VIEW_STATE_EMPTY) {
//                    findViewById(R.id.tv_fast_service).setOnClickListener(MyCommentActivity.this::onClick);
                }
            }
        });
        switch (status){
            case Contacts.ALL_ORDERS:
                rb_all.setChecked(true);
                requestData(Contacts.ALL_ORDERS,true);
                break;
            case Contacts.WAITING_PAY:
                rb_waiting_pay.setChecked(true);
                requestData(Contacts.WAITING_PAY,true);
                break;
            case Contacts.WAITING_SERVICE:
                rb_waiting_service.setChecked(true);
                requestData(Contacts.WAITING_SERVICE,true);
                break;
            case Contacts.WAITING_CONFIRM:
                rb_waiting_confirm.setChecked(true);
                requestData(Contacts.WAITING_CONFIRM,true);
                break;
            case Contacts.WAITING_COMMENT:
                rb_waiting_comment.setChecked(true);
                requestData(Contacts.WAITING_COMMENT,true);
                break;
        }
    }

    private void requestData(String requestStatus,boolean refresh) {
        if (refresh) {
            listAdapter.setEnableLoadMore(false);
        }
        ApiUserService.getOrderList(AccountHandler.getUserId(), requestStatus, refresh ? 1 : ++currentPage, Config.MAX_PAGE,new ServiceCallBack<WrapOrderBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
                if (refresh) {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setEnabled(true);
                } else {
                    --currentPage;
                }
                listAdapter.loadMoreFail();
                multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_ERROR);
            }

            @Override
            public void success(RespBean resp, Response<WrapOrderBean> payload) {
                result = payload.body().result;
//                listAdapter.setNewData(result);
//                listAdapter.notifyDataSetChanged();
                if (refresh){
                    currentPage = 1;
                    refreshLayout.setRefreshing(false);
                    if (null == result || result.size() == 0){
                        multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_EMPTY);
                    }else {
                        multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_CONTENT);
                        listAdapter.setNewData(result);
                    }
                }else {
                    listAdapter.addData(result);
                }
                if (null != result) {
                    if (result.size() < Config.MAX_PAGE) {
                        listAdapter.loadMoreEnd();
                    } else {
                        listAdapter.loadMoreComplete();
                    }
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
