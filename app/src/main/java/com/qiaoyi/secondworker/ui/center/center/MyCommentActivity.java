package com.qiaoyi.secondworker.ui.center.center;

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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommentBean;
import com.qiaoyi.secondworker.bean.WrapCommentBean;
import com.qiaoyi.secondworker.bean.WrapOrderDetailsBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.local.Config;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.map.adapter.CommentAdapter;
import com.qiaoyi.secondworker.utlis.DisplayUtil;
import com.qiaoyi.secondworker.utlis.RecyclerDecorationBox;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;
import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;

/**
 * Created on 2019/5/7
 *
 * @author Spirit
 */

public class MyCommentActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private SwipeRefreshLayout refreshLayout;
    private MultiStateLayout multiStateLayout;
    int currentPage = 1;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_mycommnet);
        initView();
        initData();
        requestData(true);
    }

    private void initData() {
        adapter = new CommentAdapter(R.layout.item_comment, this);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(BLUE);
        int itemDecorationCount = rv_list.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            rv_list.addItemDecoration(
                    new RecyclerDecorationBox(0, DisplayUtil.dip2px(this, 1), 0, 0));
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(true);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestData(false);
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
    private void requestData(boolean refresh) {
        if (refresh) {
            adapter.setEnableLoadMore(false);
        }
        ApiUserService.getMyCommentList(AccountHandler.getUserId(), refresh ? 1 : ++currentPage, Config.MAX_PAGE, new ServiceCallBack<WrapCommentBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
                if (refresh) {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setEnabled(true);
                } else {
                    --currentPage;
                }
                adapter.loadMoreFail();
                multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_ERROR);
            }

            @Override
            public void success(RespBean resp, Response<WrapCommentBean> payload) {
                List<CommentBean> result = payload.body().result;
                if (refresh){
                    currentPage = 1;
                    refreshLayout.setRefreshing(false);
                    if (null == result || result.size() == 0){
                        multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_EMPTY);
                    }else {
                        multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_CONTENT);
                        adapter.setNewData(result);
                    }
                }else {
                    adapter.addData(result);
                }
                if (null != result) {
                    if (result.size() < Config.MAX_PAGE) {
                        adapter.loadMoreEnd();
                    } else {
                        adapter.loadMoreComplete();
                    }
                }
            }
        });
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("我的评论");
        view_back.setOnClickListener(this);
        refreshLayout = findViewById(R.id.srl_wrap);
        multiStateLayout = findViewById(R.id.msl_layout);
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
