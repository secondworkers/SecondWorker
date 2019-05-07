package com.qiaoyi.secondworker.ui.center.activity;

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
import com.qiaoyi.secondworker.ui.ItemDecoration.MyItemDecoration;
import com.qiaoyi.secondworker.ui.center.adapter.MyRequirementAdapter;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.utlis.VwUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        initView();
        initData();
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

        view_back.setOnClickListener(this);
        view_right.setOnClickListener(this);
    }
        void initData(){
            rg_base.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.rb_all:
//                            request(rb_all);
                            break;
                        case R.id.rb_posting:

                            break;
                        case R.id.rb_canceled:

                            break;
                        case R.id.rb_done:

                            break;
                    }
                }
            });
            MyRequirementAdapter listAdapter = new MyRequirementAdapter(R.layout.item_service_type);
            int spacing = VwUtils.getSW(this, 2);//item间隙宽度
            int itemDecorationCount = rv_list.getItemDecorationCount();
            if (itemDecorationCount == 0) {
                rv_list.addItemDecoration(new MyItemDecoration(0, 0, 0,spacing));
                listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        //子View点击事件
                    }
                });
            }
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rv_list.setLayoutManager(manager);
            rv_list.setAdapter(listAdapter);
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
