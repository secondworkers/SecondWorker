package com.qiaoyi.secondworker.ui.center.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.pay.PayHandler;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/7/1
 *
 * @author Spirit
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back;
    private TextView tv_balance;
    private RelativeLayout rl_senior_vip;
    private ImageView iv_supreme_vip,iv_senior_vip;
    private RelativeLayout rl_supreme_vip;
    private CheckBox cb_wechat_pay;
    private TextView tv_recharge;
    private String balance;
    private double actual_price;
    private double rewardPoint;
    private EditText et_invite;
    private String pyqCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_recharge);
        balance = getIntent().getStringExtra("balance");
        VwUtils.fixScreen(this);
        initView();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        rl_senior_vip = (RelativeLayout) findViewById(R.id.rl_senior_vip);
        iv_senior_vip = (ImageView) findViewById(R.id.iv_senior_vip);
        iv_supreme_vip = (ImageView) findViewById(R.id.iv_supreme_vip);
        rl_supreme_vip = (RelativeLayout) findViewById(R.id.rl_supreme_vip);
        cb_wechat_pay = (CheckBox) findViewById(R.id.cb_wechat_pay);
        tv_recharge = (TextView) findViewById(R.id.tv_recharge);
        et_invite = findViewById(R.id.et_invite);

        view_back.setOnClickListener(this);
        rl_supreme_vip.setOnClickListener(this);
        rl_senior_vip.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        tv_balance.setText(balance);

        cb_wechat_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_recharge:
                pyqCode = et_invite.getText().toString().trim();
                PayHandler.onRequest(this,"",actual_price,rewardPoint,"",2, pyqCode);
                break;
            case R.id.rl_supreme_vip:
                rl_supreme_vip.setBackground(getResources().getDrawable(R.drawable.shape_wallet_recharge_grey));
                rl_senior_vip.setBackground(getResources().getDrawable(R.drawable.shape_wallet_recharge_no_border_grey));
                iv_supreme_vip.setVisibility(View.VISIBLE);
                iv_senior_vip.setVisibility(View.INVISIBLE);
                actual_price = 9000;
                rewardPoint = 1000;
                break;
            case R.id.rl_senior_vip:
                rl_senior_vip.setBackground(getResources().getDrawable(R.drawable.shape_wallet_recharge_grey));
                rl_supreme_vip.setBackground(getResources().getDrawable(R.drawable.shape_wallet_recharge_no_border_grey));
                iv_senior_vip.setVisibility(View.VISIBLE);
                iv_supreme_vip.setVisibility(View.INVISIBLE);
                actual_price = 900;
                rewardPoint = 100;
                break;
        }
    }
}
