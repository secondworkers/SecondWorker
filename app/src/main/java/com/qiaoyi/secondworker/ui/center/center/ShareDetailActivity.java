package com.qiaoyi.secondworker.ui.center.center;

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
import com.qiaoyi.secondworker.bean.ShareBean;
import com.qiaoyi.secondworker.bean.WrapShareBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.local.Config;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.ShareDetailAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;
import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.BLUE;

/**
 * Created on 2019/6/24
 *
 * @author Spirit
 */

public class ShareDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private SwipeRefreshLayout refreshLayout;
    private MultiStateLayout multiStateLayout;
    int currentPage = 1;
    private ShareDetailAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details);
        VwUtils.fixScreen(this);
        initView();
        initData();
        request(true);
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("邀请明细");
        refreshLayout = findViewById(R.id.srl_wrap);
        multiStateLayout = findViewById(R.id.msl_layout);

        view_back.setOnClickListener(this);
    }

    private void initData() {
        listAdapter = new ShareDetailAdapter(R.layout.item_invitation_details_white_background);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(listAdapter);
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
        ApiUserService.getShareList(AccountHandler.getmInvitCode(), refresh ? 1 : ++currentPage, Config.MAX_PAGE, new ServiceCallBack<WrapShareBean>() {
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
            public void success(RespBean resp, Response<WrapShareBean> payload) {
                List<ShareBean> list = payload.body().result.list;
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
        }
    }
}
