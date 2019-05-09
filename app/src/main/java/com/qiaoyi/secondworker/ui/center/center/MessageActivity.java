package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

/**
  create on 2019/4/24
 * ling
 * 我的信息
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private View view_title_line;
    private RecyclerView rv_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_messgge);
        initView();


    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        rv_list =  findViewById(R.id.rv_list);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_title_line = (View) findViewById(R.id.view_title_line);
        tv_title_txt.setText("我的信息");
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
