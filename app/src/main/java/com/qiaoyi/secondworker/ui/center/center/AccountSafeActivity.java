package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * create on 2019/4/25
 * ling
 * 账号与安全
 */
public class AccountSafeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_account_safe);




    }
}
