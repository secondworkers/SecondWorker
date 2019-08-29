package com.qiaoyi.secondworker.ui.shake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OnePlanBean;
import com.qiaoyi.secondworker.bean.OneUserInfo;
import com.qiaoyi.secondworker.bean.WrapOnePlanBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceDetailsActivity;
import com.qiaoyi.secondworker.ui.homepage.activity.ServiceListActivity;
import com.qiaoyi.secondworker.ui.shake.adapter.UserAdapter;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.SharePreferenceUtils;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/25
 *
 * @author Spirit
 */

public class OnePlanActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_order_total_money;
    private TextView tv_raise_total_money_china;
    private TextView tv_raise_total_money_community;
    private TextView tv_ranking_china;
    private RelativeLayout ll_community_detail;
    private TextView tv_what_one_plan;
    private TextView tv_more;
    private RecyclerView rv_list;
    private TextView tv_go_help;
    private TextView tv_ranking;
    private TextView tv_nick_name;
    private TextView tv_text;
    private ImageView iv_head_photo;
    private ProgressBar progress_bar;
    private String sqID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_plan);
        initView();
        VwUtils.fixScreen(this);
        sqID = SharePreferenceUtils.readString("sqID", "sqID");
        requestData();

    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("一元计划");
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_order_total_money = (TextView) findViewById(R.id.tv_order_total_money);
        tv_raise_total_money_china = (TextView) findViewById(R.id.tv_raise_total_money_china);
        tv_raise_total_money_community = (TextView) findViewById(R.id.tv_raise_total_money_community);
        tv_ranking_china = (TextView) findViewById(R.id.tv_ranking_china);
        ll_community_detail = (RelativeLayout) findViewById(R.id.ll_community_detail);
        tv_what_one_plan = (TextView) findViewById(R.id.tv_what_one_plan);
        tv_more = (TextView) findViewById(R.id.tv_more);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_go_help = (TextView) findViewById(R.id.tv_go_help);
        progress_bar = findViewById(R.id.progress_bar);

        tv_ranking = findViewById(R.id.tv_rank);
        tv_nick_name = findViewById(R.id.tv_nick_name);
        tv_text = findViewById(R.id.tv_text);
        iv_head_photo = findViewById(R.id.iv_head_photo);

        view_back.setOnClickListener(this);
        tv_what_one_plan.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        tv_go_help.setOnClickListener(this);
    }

    private void requestData() {
        ApiUserService.onePlan(sqID, new ServiceCallBack<WrapOnePlanBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
                //
                SharePreferenceUtils.write("sqID", "sqID","");
                finish();
            }

            @Override
            public void success(RespBean resp, Response<WrapOnePlanBean> payload) {
                OnePlanBean result = payload.body().result;
                initData(result);
            }
        });

    }

    private void initData(OnePlanBean result) {
        List<String> strings = VwUtils.splitString(String.valueOf(result.sqXiadanMoney));
        tv_1.setText(strings.get(0));
        tv_2.setText(strings.get(1));
        tv_3.setText(strings.get(2));
        tv_4.setText(strings.get(3));
        tv_5.setText(strings.get(4));
        tv_6.setText(strings.get(5));
        tv_7.setText(strings.get(6));
        tv_8.setText(strings.get(7));
        OneUserInfo info = result.userInfo;
        tv_ranking.setText("-");
        tv_nick_name.setText(info.userName);
        tv_nick_name.setTextColor(getResources().getColor(R.color.text_blue));
        tv_text.setText("贡献募集金额"+info.mujiMoney+"元");
        Glide.with(this).load(info.avatar).apply(GlideUtils.setCircleAvatar()).into(iv_head_photo);
        progress_bar.setProgress(info.precent);

        tv_raise_total_money_china.setText(String.valueOf(result.qgMujiMoney));
        tv_raise_total_money_community.setText(String.valueOf(result.sqMujiMoney));
        tv_ranking_china.setText(String.valueOf(result.sortOrder));
        UserAdapter adapter = new UserAdapter(R.layout.item_community_ranking, this);
        adapter.addData(result.userList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setAdapter(adapter);
        rv_list.setLayoutManager(manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_what_one_plan:
                startActivity(new Intent(this,OnePlanHelpActivity.class));
                break;
            case R.id.tv_more:
                startActivity(new Intent(this,CommunityMoreActivity.class));
                break;
            case R.id.tv_go_help:
                //TODO: start serviceItem id，要么点击获取ID 要么写死ID
                Intent intent = new Intent(this, ServiceListActivity.class);
                intent.putExtra("service_name","超市");
                intent.putExtra("item_id", "8ad6e1884ce34f6db3de7a84d0d1bf95");
                startActivity(intent);
                finish();
                break;
        }
    }
}
