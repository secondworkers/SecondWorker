package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;

/**
 * Created on 2019/5/7
 *
 * @author Spirit
 */

public class BecomeWorkerActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back;
    private TextView tv_release_immediately;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_become_secondworker_homepage);
        initView();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_release_immediately = findViewById(R.id.tv_release_immediately);

        view_back.setOnClickListener(this);
        tv_release_immediately.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_release_immediately:
                startActivity(new Intent(this,BecomeWorker2Activity.class));
                break;
        }
    }
}
