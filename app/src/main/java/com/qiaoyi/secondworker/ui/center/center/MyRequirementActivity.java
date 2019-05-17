package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.RequirementBean;
import com.qiaoyi.secondworker.bean.WrapRequirementBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.ItemDecoration.MyItemDecoration;
import com.qiaoyi.secondworker.ui.center.adapter.MyRequirementAdapter;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

/**
 * create on 2019/4/25
 * ling
 * 我的发布需求
 */
public class MyRequirementActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_msg;
    private RelativeLayout view_right;
    private RadioButton rb_all;
    private RadioButton rb_posting;
    private RadioButton rb_canceled;
    private RadioButton rb_done;
    private RadioGroup rg_base;
    private RecyclerView rv_list;
    private MyRequirementAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);
       VwUtils.fixScreen(this);
        initView();
        initData();
        request("");
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_msg = (ImageView) findViewById(R.id.iv_msg);
        view_right = (RelativeLayout) findViewById(R.id.view_right);
        rb_all = (RadioButton) findViewById(R.id.rb_all);
        rb_posting = (RadioButton) findViewById(R.id.rb_posting);
        rb_canceled = (RadioButton) findViewById(R.id.rb_canceled);
        rb_done = (RadioButton) findViewById(R.id.rb_done);
        rg_base = (RadioGroup) findViewById(R.id.rg_base);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("我的发布需求");
        view_back.setOnClickListener(this);
        view_right.setOnClickListener(this);
    }
        void initData(){
            rg_base.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.rb_all:
                            request("");
                            break;
                        case R.id.rb_posting:
                            request("1");
                            break;
                        case R.id.rb_canceled:
                            request("2");
                            break;
                        case R.id.rb_done:
                            request("3");
                            break;
                    }
                }
            });
            listAdapter = new MyRequirementAdapter(R.layout.item_requirement,this);
            int spacing = VwUtils.getSW(this, 2);//item间隙宽度
            int itemDecorationCount = rv_list.getItemDecorationCount();
            if (itemDecorationCount == 0) {
                rv_list.addItemDecoration(new MyItemDecoration(0, 0, 0,spacing));
            }
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rv_list.setLayoutManager(manager);
            rv_list.setAdapter(listAdapter);
        }

    private void request(String status) {
        ApiUserService.getRequirementList(AccountHandler.getUserId(), status, new ServiceCallBack<WrapRequirementBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapRequirementBean> payload) {
                List<RequirementBean> result = payload.body().result;
                listAdapter.setNewData(result);
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.view_right:
                startActivity(new Intent(this,MessageActivity.class));
                break;
        }
    }
}
