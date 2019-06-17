package com.qiaoyi.secondworker.ui.center.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.PaymentDetailsBean;
import com.qiaoyi.secondworker.bean.WrapPaymentDetailsBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.BankListAdapter;
import com.qiaoyi.secondworker.ui.center.adapter.RecordAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/5/29
 *  提现记录
 * @author Spirit
 */

public class WithdrawalRecordActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private RecordAdapter listAdapter;
    private List<PaymentDetailsBean> result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_record);
        initView();
        requestData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("提现记录");
        view_back.setOnClickListener(this);
    }

    private void requestData() {
        ApiUserService.getWithdrawalRecord(new ServiceCallBack<WrapPaymentDetailsBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapPaymentDetailsBean> payload) {
                result = payload.body().result;
                initData();
            }
        });
    }
    private void initData() {
        listAdapter = new RecordAdapter(R.layout.item_withdrawal_recodrd,this);
        listAdapter.setNewData(result);
        rv_list.setAdapter(listAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
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
