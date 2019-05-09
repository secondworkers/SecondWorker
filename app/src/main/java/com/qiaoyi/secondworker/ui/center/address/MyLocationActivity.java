package com.qiaoyi.secondworker.ui.center.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.AddressBean;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.bean.OrderConfirmEvent;
import com.qiaoyi.secondworker.bean.WrapAddressBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.adapter.AddressAdapter;
import com.qiaoyi.secondworker.utlis.DisplayUtil;
import com.qiaoyi.secondworker.utlis.RecyclerDecorationBox;
import com.qiaoyi.secondworker.utlis.VwUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.isif.alibs.utils.ToastUtils;
import cn.isif.msl.MultiStateLayout;

import static android.graphics.Color.BLUE;

/**
 * Created on 2019/4/23
 *  我的地址
 * @author Spirit
 */

public class MyLocationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private RecyclerView rv_list;
    private MultiStateLayout multiStateLayout;
    private SwipeRefreshLayout refreshLayout;
    private TextView tv_add_new;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_select_location);
        initView();
        initData();
        requestData(true);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rv_list = (RecyclerView) findViewById(R.id.rv_location);
        multiStateLayout = (MultiStateLayout) findViewById(R.id.msl_layout);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_wrap);
        tv_add_new = (TextView) findViewById(R.id.tv_add_new);
        tv_title_txt.setText("地址管理");
        view_back.setOnClickListener(this);
        tv_add_new.setOnClickListener(this);
    }
    private void initData() {
        adapter = new AddressAdapter(R.layout.item_location, this);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(BLUE);
        int itemDecorationCount = rv_list.getItemDecorationCount();
        if (itemDecorationCount == 0) {
            rv_list.addItemDecoration(
                    new RecyclerDecorationBox(0, DisplayUtil.dip2px(this, 1), 0, 0));
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(adapter);
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddressBean data = (AddressBean)adapter.getItem(position);
                EventBus.getDefault().post(new OrderConfirmEvent(data.addressname+" "+data.addressDetailName,
                        data.screenName+" "+data.addressphone,data.aid));
                finish();
               /** if (from orderConfirm){
                    EventBus.getDefault().post(new OrderConfirmEvent(data.addressname+" "+data.addressDetailName,
                            data.screenName+" "+data.addressphone,data.aid));
                    finish();
                }else if (center){
                    不执行 finish
                }*/

            }
        });
    }

    private void requestData(boolean refresh) {
        ApiUserService.getUserAddressList(AccountHandler.getUserId(), new ServiceCallBack<WrapAddressBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapAddressBean> payload) {
                WrapAddressBean body = payload.body();
                List<AddressBean> result = body.result;
                if (refresh){
//                    refreshLayout.setRefreshing(false);
                    adapter.setNewData(result);
                }else
                adapter.addData(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_add_new:
            case R.id.tv_fast_service:
                AddLocationActivity.startLocationActivity(MyLocationActivity.this,"add",0.0,0.0
                ,"","","","","");
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onAddressUpdate(MessageEvent msg){
        if (msg.getMessage().equals("add_success")){
            requestData(true);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
