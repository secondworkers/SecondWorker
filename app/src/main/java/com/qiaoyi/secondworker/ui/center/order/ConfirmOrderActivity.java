package com.qiaoyi.secondworker.ui.center.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OrderConfirmEvent;
import com.qiaoyi.secondworker.bean.PrePayOrderBean;
import com.qiaoyi.secondworker.bean.WrapPrePayOrderBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.address.MyLocationActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.datepicker.CustomDatePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/27
 *  确认订单
 * @author Spirit
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_address;
    private TextView tv_username_phone;
    private TextView tv_service_time;
    private TextView tv_service_name;
    private TextView tv_single_price;
    private TextView tv_service_time_long;
    private TextView tv_vip_discount;
    private TextView tv_total_price;
    private TextView tv_total_price1,tv_service_price;
    private TextView tv_goto_pay,tv_number;
    private RelativeLayout rl_service_location;
    private RelativeLayout rl_service_time;
    private String service_name;
    private String service_id,unit;
    private double price;
    private ImageView iv_add,iv_count,iv_service;
    private int service_num = 1;
    private CustomDatePicker timePicker;
    private String currentTime;
    private String address_id;
    private String address_title;
    private String address_msg;
    private String user_name;
    private String address_phone,service_time,worker_id,serviceItemId;
    private double total_price;

    public static void startConfirmActivity(Context context,
                                            String service_name,String service_id,
                                            String unit,double price,
                                            String worker_id,String serviceItemId){
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("service_name",service_name);
        intent.putExtra("service_id",service_id);
        intent.putExtra("price",price);
        intent.putExtra("unit",unit);
        intent.putExtra("worker_id",worker_id);
        intent.putExtra("serviceItemId",serviceItemId);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_confirm_order);
        Intent intent = getIntent();
        service_name = intent.getStringExtra("service_name");
        service_id = intent.getStringExtra("service_id");
        worker_id = intent.getStringExtra("worker_id");
        unit = intent.getStringExtra("unit");
        serviceItemId = intent.getStringExtra("serviceItemId");
        price = intent.getDoubleExtra("price",0.00);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_username_phone = (TextView) findViewById(R.id.tv_username_phone);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        tv_service_name = (TextView) findViewById(R.id.tv_service_name);
        tv_single_price = (TextView) findViewById(R.id.tv_single_price);
        tv_service_time_long = (TextView) findViewById(R.id.tv_service_time_long);
        tv_vip_discount = (TextView) findViewById(R.id.tv_vip_discount);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_total_price1 = (TextView) findViewById(R.id.tv_total_price1);
        tv_service_price = (TextView) findViewById(R.id.tv_service_price);
        tv_goto_pay = (TextView) findViewById(R.id.tv_goto_pay);
        tv_number = (TextView) findViewById(R.id.tv_number);
        iv_add = findViewById(R.id.iv_add);
        iv_count = findViewById(R.id.iv_count);
        iv_service = findViewById(R.id.iv_service);

        view_back.setOnClickListener(this);
        tv_goto_pay.setOnClickListener(this);
        rl_service_location = (RelativeLayout) findViewById(R.id.rl_service_location);
        rl_service_location.setOnClickListener(this);
        rl_service_time = (RelativeLayout) findViewById(R.id.rl_service_time);
        rl_service_time.setOnClickListener(this);

        tv_title_txt.setText("确认订单");
        tv_service_name.setText(service_name);
        tv_service_price.setText(Html.fromHtml("<font color='#FF0000'>"
                + price
                + "</font><font color='#666666'> "
                + unit
                + "</font>"));
        tv_number.setText(String.valueOf(service_num));
    }
    private void initData() {
        //"2027-12-31 23:59"
        currentTime = VwUtils.getCurrentTime();
        timePicker = new CustomDatePicker(this, "请选择服务时间", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                service_time = time;
                tv_service_time.setText(time);
            }
        }, currentTime, "2250-12-31 23:59");
        timePicker.showSpecificTime(true);
        timePicker.setIsLoop(true);
        needPay();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_goto_pay:
                createOrder();
                break;
            case R.id.rl_service_location:
                startActivity(new Intent(this,MyLocationActivity.class));
                break;
            case R.id.rl_service_time:
                timePicker.show(currentTime);
                break;
            case R.id.iv_add:
                service_num++;
                needPay();
                break;
            case R.id.iv_count:
                if (service_num > 1)
                    service_num--;
                else ToastUtils.showShort("不能再少了");
                needPay();
                break;
        }
    }

    private void createOrder() {
        if (TextUtils.isEmpty(address_id)){
            ToastUtils.showShort("请填写服务地址");
            return;
        }else if (TextUtils.isEmpty(service_time)){
            ToastUtils.showShort("请填写服务时间");
            return;
        }else
        ApiUserService.createOrder(address_id,
                service_time,
                AccountHandler.getUserId(),
                String.valueOf(service_num),
                "",
                worker_id,
                serviceItemId,
                total_price,
                new ServiceCallBack<WrapPrePayOrderBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapPrePayOrderBean> payload) {
                WrapPrePayOrderBean body = payload.body();
                PrePayOrderBean bean = body.result;
                PrePayActivity.StartPrePayActivity(ConfirmOrderActivity.this,bean.orderid,service_name,total_price);
                finish();
            }
        });
    }

    void needPay(){
        total_price = price * service_num;
        tv_single_price.setText(total_price+"元");
        tv_total_price.setText(total_price+"元");
        tv_total_price1.setText(total_price+"元");
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onAddressClick(OrderConfirmEvent event){
        tv_address.setText(event.getAddress_title_msg());
        tv_username_phone.setText(event.getAddress_name_phone());
        address_id = event.getAddress_id();
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
