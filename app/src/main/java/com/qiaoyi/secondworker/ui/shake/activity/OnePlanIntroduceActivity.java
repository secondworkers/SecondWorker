package com.qiaoyi.secondworker.ui.shake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.ui.center.center.LoginActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.SharePreferenceUtils;

/**
 * Created on 2019/7/25
 *
 * @author Spirit
 */

public class OnePlanIntroduceActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_text;
    private RelativeLayout view_back;
    private TextView tv_togo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        VwUtils.fixScreen(this);
        initView();
    }

    private void initView() {
        tv_title_text = (TextView) findViewById(R.id.tv_title_text);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_togo = (TextView) findViewById(R.id.tv_togo);

        view_back.setOnClickListener(this);
        tv_togo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_togo:
                if (AccountHandler.checkLogin()==null){
                    LoginActivity.startLoginActivity(this,000);
                }else {
                    String sqID = SharePreferenceUtils.readString("sqID", "sqID");
                    if (TextUtils.isEmpty(sqID)){
                        startActivity(new Intent(this,CommunitySelectActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(this,OnePlanActivity.class));
                        finish();
                    }
                }
                break;
        }
    }
}
