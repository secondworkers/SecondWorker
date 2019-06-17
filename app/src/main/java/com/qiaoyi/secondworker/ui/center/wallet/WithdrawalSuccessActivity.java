package com.qiaoyi.secondworker.ui.center.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/6/12
 *
 * @author Spirit
 */

public class WithdrawalSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_money;
    private TextView tv_bank;
    private TextView tv_finish;
    private String money;
    private String bank_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_bank_card);
        VwUtils.fixScreen(this);
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        bank_name = intent.getStringExtra("bank_name");
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        tv_title_txt.setText("提现申请");

        tv_bank.setText(bank_name);
        tv_money.setText(money);

        view_back.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_finish:
                finish();
                break;
        }
    }
}
