package com.qiaoyi.secondworker.ui.center.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.BaseWalletBean;
import com.qiaoyi.secondworker.bean.PaymentDetailsBean;
import com.qiaoyi.secondworker.bean.WrapWalletBean;
import com.qiaoyi.secondworker.local.Config;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.PaymentDetailsAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ToastUtils;
import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.BLUE;


/**
 * Created on 2019/5/26
 * 钱包
 * @author Spirit
 */

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back,view_more;
    private TextView tv_today_income;
    private TextView tv_total_income;
    private TextView tv_total_money;
    private RecyclerView rv_list;
    private TextView tv_withdrawal;
    private SwipeRefreshLayout refreshLayout;
    private MultiStateLayout multiStateLayout;
    int currentPage = 1;
    private PaymentDetailsAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_wallet);
        initView();
        initData();
        request(true);
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_more = (RelativeLayout) findViewById(R.id.view_more);
        tv_today_income = (TextView) findViewById(R.id.tv_today_income);
        tv_total_income = (TextView) findViewById(R.id.tv_total_income);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_withdrawal = (TextView) findViewById(R.id.tv_withdrawal);
        refreshLayout = findViewById(R.id.srl_wrap);
        multiStateLayout = findViewById(R.id.msl_layout);
        view_more.setOnClickListener(this);
        view_back.setOnClickListener(this);
        tv_withdrawal.setOnClickListener(this);
    }
    private void initData() {
        listAdapter = new PaymentDetailsAdapter(R.layout.item_payment_details,this);
        rv_list.setAdapter(listAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request(true);
            }
        });
        listAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(false);
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
    }

    private void request(boolean refresh) {
        if (refresh) {
            listAdapter.setEnableLoadMore(false);
        }
        ApiUserService.queryWallet(refresh ? 1 : ++currentPage, Config.MAX_PAGE, new ServiceCallBack<WrapWalletBean>() {
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
            public void success(RespBean resp, Response<WrapWalletBean> payload) {
                BaseWalletBean result = payload.body().result;
                tv_today_income.setText(String.valueOf(result.incomeToday));
                tv_total_income.setText(String.valueOf(result.income));
                tv_total_money.setText(String.valueOf(result.totalCash));
                List<PaymentDetailsBean> list = result.list;
                if (refresh){
                    currentPage = 1;
                    refreshLayout.setRefreshing(false);
                    if (null == list || list.size() == 0){
                        multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_EMPTY);
                    }else {
                        multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_CONTENT);
                        listAdapter.setNewData(list);
                    }
                }else {
                    listAdapter.addData(list);
                }
                if (null != list) {
                    if (list.size() < Config.MAX_PAGE) {
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
            case R.id.view_more:
                startActivity(new Intent(this,MyBankListActivity.class));
                break;
            case R.id.tv_withdrawal:
                startActivity(new Intent(MyWalletActivity.this,WithdrawalActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        request(true);
    }
}
