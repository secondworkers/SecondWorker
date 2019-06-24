package com.qiaoyi.secondworker.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommentBean;
import com.qiaoyi.secondworker.bean.ServiceItemListBean;
import com.qiaoyi.secondworker.bean.WorkerDetailsBean;
import com.qiaoyi.secondworker.bean.WorkersBean;
import com.qiaoyi.secondworker.bean.WrapWorkerDetailsBean;
import com.qiaoyi.secondworker.cache.ACache;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceDetailsActivity;
import com.qiaoyi.secondworker.ui.map.adapter.CommentAdapter;
import com.qiaoyi.secondworker.ui.map.adapter.WorkerServiceListAdapter;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

/**
 * Created on 2019/5/12
 * 工人主页
 *
 * @author Spirit
 */

public class WorkerCenterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private ImageView iv_head_picture;
    private TextView tv_username;
    private TextView tv_order;
    private TextView tv_location;
    private TextView tv_shop;
    private TextView tv_grade;
    private TextView tv_self_introduction;
    private RelativeLayout rl_self_introduction;
    private TextView tv_service_project;
    private RelativeLayout rl_service_project;
    private TextView tv_interval;
    private TextView tv_user_evaluation;
    private RecyclerView rv_user_evaluationt, rv_service;
    private RelativeLayout user_evaluation;
    private RelativeLayout total;
    private String worker_id;
    private List<CommentBean> commentList;
    private List<ServiceItemListBean> serviceItemList;
    private String workerId;
    private String rate;
    private int counts;
    private String workerName;
    private String profile;
    private RelativeLayout view_back;
    private double range;
    private String icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        Intent intent = getIntent();
        worker_id = intent.getStringExtra("worker_id");
        setContentView(R.layout.activity_personal_homepage);
        ACache mCache = ACache.get(getApplication());
        lat = Double.valueOf(mCache.getAsString("user_loc_lat"));
        lng = Double.valueOf(mCache.getAsString("user_loc_lng"));
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }
    private void requestData() {
        ApiHome.getWorkerDetail(worker_id, lng, lat, new ServiceCallBack<WrapWorkerDetailsBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapWorkerDetailsBean> payload) {
                WorkerDetailsBean result = payload.body().result;
                commentList = result.listev;
                serviceItemList = result.listItem;
                WorkersBean workersBean = result.mgWorker;
//                rate = workersBean.rate;
                icon = workersBean.icon;
                range = workersBean.range;
                counts = workersBean.counts;
                workerName = workersBean.workerName;
                profile = workersBean.profile;
                initData();
            }
        });
    }

    private void initData() {
        tv_title_txt.setText("个人主页");
        tv_username.setText(workerName);
        Glide.with(this).load(icon).apply(GlideUtils.setCircleAvatar()).into(iv_head_picture);
        tv_order.setText("已服务" + counts + "单  好评" + rate+"%");
        tv_self_introduction.setText(profile);
        tv_location.setText(String.valueOf(range)+"km");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_service.setLayoutManager(layoutManager);
        WorkerServiceListAdapter serviceListAdapter = new WorkerServiceListAdapter(R.layout.item_collect, this);
        serviceListAdapter.addData(serviceItemList);
        rv_service.setAdapter(serviceListAdapter);
        rv_service.setNestedScrollingEnabled(false);
        rv_service.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ServiceItemListBean item = (ServiceItemListBean) adapter.getItem(position);
                Intent intent = new Intent(WorkerCenterActivity.this, ServiceDetailsActivity.class);
                intent.putExtra("serviceItemId", item.goodsId);
                startActivity(intent);

            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        CommentAdapter commentAdapter = new CommentAdapter(R.layout.item_comment, this);
        rv_user_evaluationt.setLayoutManager(layoutManager2);
        commentAdapter.addData(commentList);
        rv_user_evaluationt.setAdapter(commentAdapter);
        rv_user_evaluationt.setNestedScrollingEnabled(false);
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        iv_head_picture = (ImageView) findViewById(R.id.iv_head_picture);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_order = (TextView) findViewById(R.id.tv_order);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_self_introduction = (TextView) findViewById(R.id.tv_self_introduction);
        rl_self_introduction = (RelativeLayout) findViewById(R.id.rl_self_introduction);
        tv_service_project = (TextView) findViewById(R.id.tv_service_project);
        rl_service_project = (RelativeLayout) findViewById(R.id.rl_service_project);
        tv_interval = (TextView) findViewById(R.id.tv_interval);
        tv_user_evaluation = (TextView) findViewById(R.id.tv_user_evaluation);
        rv_user_evaluationt = (RecyclerView) findViewById(R.id.rv_user_evaluationt);
        rv_service = (RecyclerView) findViewById(R.id.rv_service);
        user_evaluation = (RelativeLayout) findViewById(R.id.user_evaluation);
        total = (RelativeLayout) findViewById(R.id.total);

        tv_shop.setOnClickListener(this);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shop:

                break;
            case R.id.view_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
