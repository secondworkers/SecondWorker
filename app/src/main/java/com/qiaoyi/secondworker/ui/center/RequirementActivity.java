package com.qiaoyi.secondworker.ui.center;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;

/**
 * create on 2019/4/25
 * ling
 * 我的发布需求
 */
public class RequirementActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);

    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("我的发布需求");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_right:

                break;
        }
    }
}
