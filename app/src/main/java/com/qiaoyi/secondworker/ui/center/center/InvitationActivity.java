package com.qiaoyi.secondworker.ui.center.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.ShareBaseBean;
import com.qiaoyi.secondworker.bean.ShareBean;
import com.qiaoyi.secondworker.bean.WrapShareBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.ShareListAdapter;
import com.qiaoyi.secondworker.ui.center.wallet.MyWalletActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/6/22
 *
 * @author Spirit
 */

public class InvitationActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back;
    private TextView tv_invitation_code;
    private TextView tv_copy;
    private TextView tv_invite_count;
    private TextView tv_count;
    private TextView tv_money;
    private TextView tv_look_more;
    private RecyclerView rv_list;
    private TextView tv_share_wechat;
    private TextView tv_share_circle;
    private TextView tv_qrCode,rl_bottom,tv_cash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_activity);
        VwUtils.fixScreen(this);
        initView();
        requestData();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_invitation_code = (TextView) findViewById(R.id.tv_invitation_code);
        tv_copy = (TextView) findViewById(R.id.tv_copy);
        tv_invite_count = (TextView) findViewById(R.id.tv_invite_count);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_look_more = (TextView) findViewById(R.id.tv_look_more);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_share_wechat = (TextView) findViewById(R.id.tv_share_wechat);
        tv_share_circle = (TextView) findViewById(R.id.tv_share_circle);
        tv_qrCode = (TextView) findViewById(R.id.tv_qrCode);
        rl_bottom = (TextView) findViewById(R.id.tv_share);
        tv_cash = (TextView) findViewById(R.id.tv_cash);
        tv_invitation_code.setText("专属邀请码："+AccountHandler.getmInvitCode());

        tv_cash.setOnClickListener(this);
        view_back.setOnClickListener(this);
        rl_bottom.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        tv_look_more.setOnClickListener(this);
        tv_share_wechat.setOnClickListener(this);
        tv_share_circle.setOnClickListener(this);
        tv_qrCode.setOnClickListener(this);
    }

    private void requestData() {
        ApiUserService.getShareList(AccountHandler.getmInvitCode(), 0, 3, new ServiceCallBack<WrapShareBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapShareBean> payload) {
                ShareBaseBean result = payload.body().result;
                initData(result);
            }
        });
    }

    private void initData(ShareBaseBean result) {
        int counts = result.counts;
        double money = result.money;
        List<ShareBean> list = result.list;
        tv_money.setText(money+"元");
        tv_count.setText(counts+"位");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        ShareListAdapter listAdapter = new ShareListAdapter(R.layout.item_invitation_details_purple_background);
        listAdapter.addData(list);
        rv_list.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cash:
                startActivity(new Intent(this, MyWalletActivity.class));
                break;
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_copy:
                ClipboardManager cm =(ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(AccountHandler.getmInvitCode());
                ToastUtils.showShort("已复制");
                break;
            case R.id.tv_look_more:
                startActivity(new Intent(this,ShareDetailActivity.class));
                break;
            case R.id.tv_share:
                startActivity(new Intent(this,ShareInvitationActivity.class));
                break;
            case R.id.tv_share_wechat:

                break;
            case R.id.tv_share_circle:

                break;
            case R.id.tv_qrCode:

                break;
        }
    }
}
