package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * create on 2019/4/25
 * ling
 * 账号与安全
 */
public class AccountSafeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_wechat;
    private TextView tv_isbound;
    private TextView tv_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_account_safe);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_wechat = (ImageView) findViewById(R.id.iv_wechat);
        tv_isbound = (TextView) findViewById(R.id.tv_isbound);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        if (AccountHandler.getUserState() == AccountHandler.LOGIN_TYPE.THIRD){//需要后台配合输出字段
            tv_isbound.setText("已绑定");
        }
        tv_phone_number.setText(AccountHandler.getUserPhone());
        view_back.setOnClickListener(this);
        tv_isbound.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_isbound:
                finish();
                break;
        }
    }
}
