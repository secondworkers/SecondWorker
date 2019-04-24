package com.qiaoyi.secondworker.ui.center;

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
import com.qiaoyi.secondworker.bean.AddressBean;
import com.qiaoyi.secondworker.ui.center.adapter.AddressAdapter;
import com.qiaoyi.secondworker.utlis.DisplayUtil;
import com.qiaoyi.secondworker.utlis.RecyclerDecorationBox;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.GREEN;

/**
 * Created on 2019/4/23
 *  地址管理
 * @author Spirit
 */

public class SelectLocationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private MultiStateLayout multiStateLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView tv_add_new;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_select_location);
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_location);
        multiStateLayout = (MultiStateLayout) findViewById(R.id.msl_layout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_wrap);
        tv_add_new = (TextView) findViewById(R.id.tv_add_new);
        tv_title_txt.setText("地址管理");
        view_back.setOnClickListener(this);
        tv_add_new.setOnClickListener(this);
    }
    private void initData() {
        adapter = new AddressAdapter(R.layout.item_location, this);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(GREEN);
        int itemDecorationCount = rv_list.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            rv_list.addItemDecoration(
                    new RecyclerDecorationBox(0, DisplayUtil.dip2px(this, 1), 0, 0));
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(adapter);
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddressBean data = (AddressBean)adapter.getItem(position);
//                if ("2".equals(data.comment_status)&&"1".equals(data.answer_status)){
//                    Intent intent = new Intent(MyServiceActivity.this, ServiceCommentActivity.class);
//                    intent.putExtra("oid",data.oid);
//                    intent.putExtra("crops_name",data.crops_name);
//                    intent.putExtra("order_serve_time",data.order_serve_time);
//                    startActivity(intent);
//                }else {
//                    return;
//                }
            }
        });
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
                    findViewById(R.id.tv_fast_service).setOnClickListener(SelectLocationActivity.this::onClick);
                }
            }
        });
    }

    private void requestData(boolean b) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_add_new:
            case R.id.tv_fast_service:
                LocationActivity.startLocationActivity(SelectLocationActivity.this,"add");
                break;
        }
    }
}
