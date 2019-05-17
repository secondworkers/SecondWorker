package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/5/7
 * 我的收藏
 *
 * @author Spirit
 */

public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private TextView tv_title_more;
    private RecyclerView rv_list;
    private RelativeLayout view_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_collection);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_more = (TextView) findViewById(R.id.tv_title_more);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_more.setText("编辑");
        tv_title_txt.setText("我的收藏");
        view_back = (RelativeLayout) findViewById(R.id.view_back);
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
