package com.qiaoyi.secondworker.ui.shake.activity;

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
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OnePlanBean;
import com.qiaoyi.secondworker.bean.OneUserInfo;
import com.qiaoyi.secondworker.bean.WrapOnePlanBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.shake.adapter.UserAdapter;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.SharePreferenceUtils;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/27
 *
 * @author Spirit
 */

public class CommunityMoreActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private String sqID;
    private TextView tv_ranking;
    private TextView tv_nick_name;
    private TextView tv_text;
    private ImageView iv_head_photo;
    private ProgressBar progress_bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_ranking);
        VwUtils.fixScreen(this);
        initView();
        sqID = SharePreferenceUtils.readString("sqID", "sqID");
        requestData();

    }

    private void requestData() {
        ApiUserService.onePlan(sqID, new ServiceCallBack<WrapOnePlanBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapOnePlanBean> payload) {
                OnePlanBean result = payload.body().result;
                initData(result);
            }
        });

    }
    private void initData(OnePlanBean result) {
        OneUserInfo info = result.userInfo;
        tv_ranking.setText("-");
        tv_nick_name.setText(info.userName);
        tv_nick_name.setTextColor(getResources().getColor(R.color.text_blue));
        tv_text.setText("贡献募集金额"+info.mujiMoney+"元");
        Glide.with(this).load(info.avatar).apply(GlideUtils.setCircleAvatar()).into(iv_head_photo);
        progress_bar.setProgress(info.precent);
        UserAdapter adapter = new UserAdapter(R.layout.item_community_ranking, this);
        adapter.addData(result.userList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setAdapter(adapter);
        rv_list.setLayoutManager(manager);
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("社区排行");
        tv_ranking = findViewById(R.id.tv_rank);
        tv_nick_name = findViewById(R.id.tv_nick_name);
        tv_text = findViewById(R.id.tv_text);
        iv_head_photo = findViewById(R.id.iv_head_photo);
        progress_bar = findViewById(R.id.progress_bar);
        view_back.setOnClickListener(this);
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
