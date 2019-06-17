package com.qiaoyi.secondworker.ui.center.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.BankBean;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.bean.WrapBankBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.BankListAdapter;
import com.qiaoyi.secondworker.utlis.VwUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.StringUtils;

/**
 * Created on 2019/5/29
 * 银行列表
 *
 * @author Spirit
 */

public class MyBankListActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt,tv_add_card;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private BankListAdapter listAdapter;
    private List<BankBean> result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_bank_list);
        EventBus.getDefault().register(this);
        initView();
        requestData();

    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_add_card = (TextView) findViewById(R.id.tv_add_card);
        tv_add_card = (TextView) findViewById(R.id.tv_add_card);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_title_txt.setText("银行卡");
        view_back.setOnClickListener(this);
        tv_add_card.setOnClickListener(this);
    }

    private void requestData() {
        ApiUserService.getBindBankCard(new ServiceCallBack<WrapBankBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapBankBean> payload) {
                result = payload.body().result;
//                VwUtils.getLast4String();
                initData();
            }
        });
    }

    private void initData() {
        listAdapter = new BankListAdapter(R.layout.item_bank,this);
        listAdapter.setNewData(result);
        rv_list.setAdapter(listAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                BankBean item = (BankBean) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("bank_name",BankConfigConstant.Bank_ITEM_NAME.get(item.bankCode));
                intent.putExtra("bank_num",item.bankCardNo);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindBankFinish(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("fresh")){
            requestData();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
                case R.id.tv_add_card:
                    startActivity(new Intent(this,AddBankCardActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
