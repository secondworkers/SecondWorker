package com.qiaoyi.secondworker.ui.center.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/29
 *
 * @author Spirit
 */

public class AddBankCardActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private EditText et_name;
    private EditText et_Id;
    private EditText et_bank_number;
    private RelativeLayout select_bank;
    private EditText et_phone_number;
    private TextView tv_done;
    private String bankName;
    private String bankCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_add_bank);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        et_name = (EditText) findViewById(R.id.et_name);
        et_Id = (EditText) findViewById(R.id.et_Id);
        et_bank_number = (EditText) findViewById(R.id.et_bank_number);
        select_bank = (RelativeLayout) findViewById(R.id.select_bank);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_title_txt.setText("添加银行卡");
        view_back.setOnClickListener(this);
        tv_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_done:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String name = et_name.getText().toString().trim();
        String idCard = et_Id.getText().toString().trim();
        String bankId = et_bank_number.getText().toString().trim();
        String phone_number = et_phone_number.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入持卡人姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            Toast.makeText(this, "请输入您的身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bankId)) {
            Toast.makeText(this, "请输入您的银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone_number)) {
            Toast.makeText(this, "请输入银行预留手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String statStr = "ok";
        // TODO validate success, do something
        ApiUserService.getBankInfo(bankId, new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }
            @Override
            public void success(RespBean resp, Response payload) {
                String body = (String) payload.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                   if (jsonObject.get("stat").equals(statStr)){
                       bankCode = (String) jsonObject.get("bank");
                       if(!(bankCode ==null)) {
                           bankName = BankConfigConstant.Bank_ITEM_NAME.get(bankCode);
                           ApiUserService.bindBankCard(name, idCard, bankId, bankCode, phone_number, Contact.BASE_BANK_IMG + bankCode, new ServiceCallBack() {
                               @Override
                               public void failed(String code, String errorInfo, String source) {
                                   ToastUtils.showShort(errorInfo);
                                   return;
                               }

                               @Override
                               public void success(RespBean resp, Response payload) {
                                   EventBus.getDefault().post(new MessageEvent("fresh"));
                                   finish();
                               }
                           });
                       }else{
                           bankName = BankConfigConstant.Bank_type;
                           ToastUtils.showShort(bankName);
                       }
                   } else {
                       bankName = BankConfigConstant.Bank_type;
                       ToastUtils.showShort(bankName);
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
