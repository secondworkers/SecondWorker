package com.qiaoyi.secondworker.ui.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.CommentBean;
import com.qiaoyi.secondworker.bean.ServiceDetailsBean;
import com.qiaoyi.secondworker.bean.ServiceItemDetail;
import com.qiaoyi.secondworker.bean.WrapServiceDetailsBean;
import com.qiaoyi.secondworker.bean.WrapServiceItemBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiHome;
import com.qiaoyi.secondworker.ui.center.center.BindMobileActivity;
import com.qiaoyi.secondworker.ui.center.center.LoginActivity;
import com.qiaoyi.secondworker.ui.center.order.ConfirmOrderActivity;
import com.qiaoyi.secondworker.ui.map.adapter.CommentAdapter;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/14
 * 服务详情
 *
 * @author Spirit
 */

public class ServiceDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_placeholder;
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_shop_image;
    private TextView tv_service_type;
    private TextView tv_service_detail;
    private TextView tv_service_price;
    private TextView tv_service_time;
    private TextView tv_order;
    private TextView tv_service;
    private TextView tv_else;
    private ImageView iv_shop_logo;
    private TextView tv_shop_name;
    private RatingBar ratingbar;
    private TextView tv_score;
    private TextView tv_come_shop;
    private TextView tv_content_1;
    private RelativeLayout rl_center;
    private TextView tv_content_2;
    private TextView tv_comment_number;
    private TextView tv_place_order;
    private RecyclerView rv_comment;
    private String serviceItemId,worker_id;
    private ServiceItemDetail serviceItemDetail;
    private List<CommentBean> evDetail;
    private CommentAdapter commentAdapter;
    private int orderCounts;
    private int evCounts;

    public static void startDetails(Activity activity, String serviceItemId,String worker_id) {
        Intent intent = new Intent(activity, ServiceDetailsActivity.class);
        intent.putExtra("serviceItemId", serviceItemId);
        intent.putExtra("worker_id", worker_id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_service_details);
        Intent intent = getIntent();
        serviceItemId = intent.getStringExtra("serviceItemId");
        worker_id = intent.getStringExtra("worker_id");
        toStartLocation();
        initView();
        requestData();
//        initData();
    }

    private void requestData() {
        ApiHome.getServiceDetail(serviceItemId, new ServiceCallBack<WrapServiceDetailsBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapServiceDetailsBean> payload) {
                ServiceDetailsBean result = payload.body().result;
                evCounts = result.evCounts;
//                orderCounts = result.orderCounts;
                serviceItemDetail = result.serviceItemDetail;
                evDetail = result.evDetail;
                initData();
            }
        });
    }

    private void initData() {
        tv_title_txt.setText("服务详情");
        tv_order.setText("已售"+serviceItemDetail.count+"单");
        tv_comment_number.setText(evCounts+"条评价");
        Glide.with(this).load(serviceItemDetail.goodsPhoto).apply(GlideUtils.setRoundTransform(this,10)).into(iv_shop_image);
        tv_service_type.setText(serviceItemDetail.goodsName);
        if (!TextUtils.isEmpty(serviceItemDetail.goodsInfo))
        tv_service_detail.setText(serviceItemDetail.goodsInfo);
        tv_service.setText(serviceItemDetail.goodsName);
        tv_service_price.setText(serviceItemDetail.price + serviceItemDetail.unit);

        tv_content_1.setText(serviceItemDetail.goodsDescription);
        tv_content_2.setText(serviceItemDetail.serviceTenet);
        commentAdapter = new CommentAdapter(R.layout.item_comment, this);
        commentAdapter.addData(evDetail);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_comment.setLayoutManager(manager);
        rv_comment.setNestedScrollingEnabled(false);
        rv_comment.setAdapter(commentAdapter);

    }

    private void initView() {
        tv_placeholder = (TextView) findViewById(R.id.tv_placeholder);
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_shop_image = (ImageView) findViewById(R.id.iv_shop_image);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_service_detail = (TextView) findViewById(R.id.tv_service_detail);
        tv_service_price = (TextView) findViewById(R.id.tv_service_price);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        tv_order = (TextView) findViewById(R.id.tv_order);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_else = (TextView) findViewById(R.id.tv_else);
        iv_shop_logo = (ImageView) findViewById(R.id.iv_shop_logo);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_come_shop = (TextView) findViewById(R.id.tv_come_shop);
        tv_content_1 = (TextView) findViewById(R.id.tv_content_1);
        rl_center = (RelativeLayout) findViewById(R.id.rl_center);
        tv_content_2 = (TextView) findViewById(R.id.tv_content_2);
        tv_comment_number = (TextView) findViewById(R.id.tv_comment_number);
        tv_place_order = (TextView) findViewById(R.id.tv_place_order);
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);

        view_back.setOnClickListener(this);
        tv_else.setOnClickListener(this);
        tv_come_shop.setOnClickListener(this);
        tv_place_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_else:

                break;
            case R.id.tv_come_shop:

                break;
            case R.id.tv_place_order:
                if (AccountHandler.checkLogin()!=null) {
                        ConfirmOrderActivity.startConfirmActivity(this, serviceItemDetail.goodsName, worker_id
                                , serviceItemDetail.unit, serviceItemDetail.price, serviceItemDetail.orgId, serviceItemDetail.goodsId);
                }else {
                    LoginActivity.startLoginActivity(this,9010);
                }
                break;
        }
    }
}
