package com.qiaoyi.secondworker.ui.center.wallet;

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
import com.qiaoyi.secondworker.bean.BaseRewardpointBean;
import com.qiaoyi.secondworker.bean.RewardpointBean;
import com.qiaoyi.secondworker.bean.WrapRewardpointBean;
import com.qiaoyi.secondworker.local.Config;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.MRecordAdapter;
import com.qiaoyi.secondworker.ui.center.adapter.RecordAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;
import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.BLUE;

/**
 * Created on 2019/6/11
 * 积分明细
 *
 * @author Spirit
 */

public class MyIntegralActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_useful_integral;
    private RecyclerView rv_list;
    private List<RewardpointBean> list;
    private double rewardPoints;
    private SwipeRefreshLayout refreshLayout;
    private MultiStateLayout multiStateLayout;
    int currentPage = 1;
    private MRecordAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intergral);
        VwUtils.fixScreen(this);
        initView();
        initData();
        request(true);
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_useful_integral = (TextView) findViewById(R.id.tv_useful_integral);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        refreshLayout = findViewById(R.id.srl_wrap);
        multiStateLayout = findViewById(R.id.msl_layout);
        tv_title_txt.setText("我的积分");
        view_back.setOnClickListener(this);
    }

    private void request(boolean refresh) {
        if (refresh) {
            listAdapter.setEnableLoadMore(false);
        }
        ApiUserService.getRewardpointList(refresh ? 1 : ++currentPage, Config.MAX_PAGE,new ServiceCallBack<WrapRewardpointBean>() {
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
            public void success(RespBean resp, Response<WrapRewardpointBean> payload) {
                BaseRewardpointBean result = payload.body().result;
                rewardPoints = result.rewardPoints;
                list = result.list;
                tv_useful_integral.setText(String.valueOf(rewardPoints));
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

    private void initData() {

        listAdapter = new MRecordAdapter(R.layout.item_withdrawal_recodrd,this);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(BLUE);
        rv_list.setAdapter(listAdapter);
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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }
}
