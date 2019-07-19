package com.qiaoyi.secondworker.ui.center.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.BankBean;
import com.qiaoyi.secondworker.bean.WrapBankBean;
import com.qiaoyi.secondworker.bean.WrapCashBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/29
 * 提现
 *
 * @author Spirit
 */

public class WithdrawalActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back,view_more;
    private TextView tv_today_income;
    private TextView tv_withdrawal_record;
    private RadioButton rb_bank_card;
    private RadioButton rb_wechat;
    private RadioButton rb_alipay;
    private RadioGroup radio_group;
    private ImageView iv_bank;
    private RelativeLayout rl_bank;
    private EditText et_num;
    private TextView tv_apply_withdrawal,tv_bank_card,tv_apply_all;
    private int checkId = 1;
    private String bank_num ="";
    private double totalCash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_withdrawal);
        initView();
        requestData();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_more = (RelativeLayout) findViewById(R.id.view_more);
        tv_today_income = (TextView) findViewById(R.id.tv_today_income);
        tv_withdrawal_record = (TextView) findViewById(R.id.tv_withdrawal_record);
        rb_bank_card = (RadioButton) findViewById(R.id.rb_bank_card);
        rb_wechat = (RadioButton) findViewById(R.id.rb_wechat);
        rb_alipay = (RadioButton) findViewById(R.id.rb_alipay);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        iv_bank = (ImageView) findViewById(R.id.iv_bank);
        rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
        et_num = (EditText) findViewById(R.id.et_num);
        tv_apply_withdrawal = (TextView) findViewById(R.id.tv_apply_withdrawal);
        tv_bank_card = (TextView) findViewById(R.id.tv_bank_card);
        tv_apply_all = (TextView) findViewById(R.id.tv_apply_all);

        view_more.setOnClickListener(this);
        tv_apply_all.setOnClickListener(this);
        view_back.setOnClickListener(this);
        tv_withdrawal_record.setOnClickListener(this);
        rl_bank.setOnClickListener(this);
        tv_apply_withdrawal.setOnClickListener(this);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_bank_card:
                        rl_bank.setVisibility(View.VISIBLE);
                        checkId = 1;
                        break;
                    case R.id.rb_wechat:
                        rl_bank.setVisibility(View.GONE);
                        checkId = 2;
                        break;
                    case R.id.rb_alipay:
                        rl_bank.setVisibility(View.GONE);
                        checkId = 3;
                        break;
                }
            }
        });
    }

    private void requestData() {
        ApiUserService.gotoWithdrawal(new ServiceCallBack<WrapCashBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showLong(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapCashBean> payload) {
                totalCash = payload.body().result.totalCash;
                tv_today_income.setText(String.valueOf(totalCash));
                List<BankBean> cardList = payload.body().result.cardList;
                if (cardList!=null&&cardList.size()>0){
                    BankBean bankBean = cardList.get(0);
                    tv_bank_card.setText(BankConfigConstant.Bank_ITEM_NAME.get(bankBean.bankCode)+"("+bankBean.bankCardNo.substring(bankBean.bankCardNo.length()-4,bankBean.bankCardNo.length())+")");
                    bank_num = bankBean.bankCardNo;
                }
                et_num.setHint("可提现金额"+ totalCash +"元");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_apply_all:
               et_num.setText(tv_today_income.getText());
                break;
            case R.id.view_more:
                startActivity(new Intent(this,MyBankListActivity.class));
                break;
            case R.id.tv_withdrawal_record:
                startActivity(new Intent(this,WithdrawalRecordActivity.class));
                break;
            case R.id.rl_bank:
                startActivityForResult(new Intent(this,MyBankListActivity.class),1990);
                break;
            case R.id.tv_apply_withdrawal:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String num = et_num.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            return;
        }else {
            // TODO validate success, do something
            double v = totalCash - Double.valueOf(num);
            if (Double.valueOf(num)%100 != 0){
                ToastUtils.showShort("输入金额应为100整数倍");
                return;
            }
            if (0 >= v){
                ToastUtils.showShort("可提现金额不足");
                return;
            }else {
                if (TextUtils.isEmpty(bank_num)){
                    ToastUtils.showShort("请选择提现银行卡");
                    return;
                }else {
                    ApiUserService.applyWithdrawal(bank_num, num, new ServiceCallBack() {
                        @Override
                        public void failed(String code, String errorInfo, String source) {
                            ToastUtils.showShort(errorInfo);
                        }
                        @Override
                        public void success(RespBean resp, Response payload) {
                            Intent intent = new Intent(WithdrawalActivity.this, WithdrawalSuccessActivity.class);
                            intent.putExtra("money",String.valueOf(num));
                            intent.putExtra("bank_name",tv_bank_card.getText());
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            String bank_name = data.getStringExtra("bank_name");
            bank_num = data.getStringExtra("bank_num");
            String bankId = bank_num.substring(bank_num.length()-4, bank_num.length());
            tv_bank_card.setText(bank_name+"("+bankId+")");
        }

    }
}
