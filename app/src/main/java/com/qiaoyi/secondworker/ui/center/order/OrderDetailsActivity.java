package com.qiaoyi.secondworker.ui.center.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OrderBean;
import com.qiaoyi.secondworker.bean.WrapOrderDetailsBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.CircularImageView;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/24
 *  订单详情
 * @author Spirit
 */

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private ImageView iv_title_back;
    private RelativeLayout view_back;
    private CircularImageView iv_avatar;
    private TextView tv_username;
    private TextView tv_aervice_type;
    private TextView tv_arrive_time;
    private TextView tv_address;
    private TextView tv_username_phone;
    private TextView tv_service_time;
    private TextView tv_service_name;
    private TextView tv_single_price;
    private TextView tv_total_price;
    private TextView tv_order_msg;
    private TextView tv_order_id;
    private TextView tv_order_copy_id;
    private TextView tv_order_create_time;
    private TextView tv_order_pay_time,tv_order_pay_time_title,tv_vip_discount,tv_order_finish_time,tv_order_finish_time_title;
    private TextView tv_left_btn;
    private TextView tv_right_btn;
    private String order_id;//order status
    private OrderBean result;

    public static void gotoOrderDetails(Activity activity,String order_id){
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra("order_id",order_id);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetatil);
        VwUtils.fixScreen(this);
        order_id = getIntent().getStringExtra("order_id");
        initView();
        requestData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_avatar = (CircularImageView) findViewById(R.id.iv_avatar);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_aervice_type = (TextView) findViewById(R.id.tv_aervice_type);
        tv_arrive_time = (TextView) findViewById(R.id.tv_arrive_time);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_username_phone = (TextView) findViewById(R.id.tv_username_phone);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        tv_service_name = (TextView) findViewById(R.id.tv_service_name);
        tv_single_price = (TextView) findViewById(R.id.tv_single_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_order_msg = (TextView) findViewById(R.id.tv_order_msg);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_order_copy_id = (TextView) findViewById(R.id.tv_order_copy_id);
        tv_order_create_time = (TextView) findViewById(R.id.tv_order_create_time);
        tv_order_pay_time = (TextView) findViewById(R.id.tv_order_pay_time);
        tv_order_pay_time_title = (TextView) findViewById(R.id.tv_order_pay_time_title);
        tv_order_finish_time = (TextView) findViewById(R.id.tv_order_finish_time);
        tv_order_finish_time_title = (TextView) findViewById(R.id.tv_order_finish_time_title);
        tv_left_btn = (TextView) findViewById(R.id.tv_left_btn);
        tv_right_btn = (TextView) findViewById(R.id.tv_right_btn);
        tv_vip_discount = (TextView) findViewById(R.id.tv_vip_discount);
        tv_title_txt.setText("订单详情");
        view_back.setOnClickListener(this);
        tv_left_btn.setOnClickListener(this);
        tv_right_btn.setOnClickListener(this);
        tv_order_copy_id.setOnClickListener(this);
    }
    private void initData(OrderBean result) {
        changeByStatus(result);
        tv_service_name.setText(result.serviceItem);
        tv_arrive_time.setText("预计"+result.serviceTime+"到达");
        tv_service_time.setText(result.serviceTime);
        tv_address.setText(result.addressname+" "+result.addressDetailName);
        tv_username_phone.setText(result.screenName+" "+result.addressphone);
        tv_single_price.setText(Html.fromHtml("<font color='#212121'>"
                + result.serviceItem
                + "</font><font color='#666666'> ("
                + result.price+ result.unit
                + ")</font>"));
        tv_vip_discount.setText("X"+result.payCount);//服务次数
        tv_total_price.setText(Html.fromHtml("<font color='#666666'>总价 "
                + "</font><font color='#ff0000'><big> "
                + result.actualPay
                + "元</big></font>"));
        tv_order_id.setText(result.orderNum);
        tv_order_create_time.setText(result.creatime);
        if (TextUtils.isEmpty(result.paytime)){
            tv_order_pay_time_title.setVisibility(View.GONE);
            tv_order_pay_time.setVisibility(View.GONE);
        }
        tv_order_pay_time.setText(result.paytime);
    }
    private void requestData() {
        ApiUserService.getOrderDetails(order_id, new ServiceCallBack<WrapOrderDetailsBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapOrderDetailsBean> payload) {
                result = payload.body().result;
                initData(result);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_left_btn:
                onLeftBtnClick(result);
                break;
            case R.id.tv_right_btn:
                btnRightClick(result);
                break;
            case R.id.tv_order_copy_id://copy to clipboard
                ClipboardManager cm =(ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(result.orderNum);
            ToastUtils.showShort("已复制");
                break;
        }
    }
    private void cancelOrDelOrder(OrderBean bean,int status) {
        ApiUserService.cancelAndDelOrder(bean.orderid, status, new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {
            }
            @Override
            public void success(RespBean resp, Response payload) {
                if (status == 8) {//删除订单
                    ToastUtils.showShort("订单已删除");
                    finish();
                }else if (status == 7 ){//取消订单
                    bean.setStatus(7);
                    requestData();
                }else if (status == 10){
                    bean.setStatus(10);
                    requestData();
                }
            }
        });
    }
    void onLeftBtnClick(OrderBean item){
        switch (item.status){
            case 0:
                cancelOrDelOrder(item,7);
                break;
            case 1://待服务
            case 2:
            case 3:
            case 4:
                ToastUtils.showShort("已催单");
                break;
            case 5://待确认
            case 6:
            case 9:
                startActivity(new Intent(this, MainActivity.class));
               finish();
                break;
            case 7://已取消
            case 10://已评价
                cancelOrDelOrder(item,8);
                break;
        }
    }
    private void btnRightClick(OrderBean item) {
        switch (item.status){
            case 0:
                PrePayActivity.StartPrePayActivity(this,item.orderid,item.serviceItem,item.actualPay);
                finish();
                break;
            case 1://待服务
            case 2:
            case 3:
            case 4:
                ToastUtils.showShort("已催单");
                break;
            case 5://待确认
            case 6:
                cancelOrDelOrder(item,10);
                break;
            case 9://评价
                ToastUtils.showShort("评价");
//                OrderDetailsActivity.gotoOrderDetails(this,item.orderid);
                Intent intent = new Intent(this, PostCommentActivity.class);
                intent.putExtra("serviceItem",item.serviceItem);
                intent.putExtra("orderId",item.orderid);
                startActivity(intent);
//                finish();
                break;
            case 7://取消、删除
            case 8:
            case 10:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
    private void changeByStatus(OrderBean item) {
        switch (item.status){
            case 0:
                iv_avatar.setVisibility(View.GONE);
                tv_username.setText("待支付");//根据status 修改 TODO: 明天修改 button 和 title
                tv_arrive_time.setText("请在15分钟内完成支付");
                tv_left_btn.setText("取消订单");
                tv_right_btn.setText("去支付");
                tv_order_finish_time.setVisibility(View.GONE);
                tv_order_finish_time_title.setVisibility(View.GONE);
                tv_order_pay_time_title.setVisibility(View.GONE);
                tv_order_pay_time.setVisibility(View.GONE);
                break;
            case 1://待服务
                iv_avatar.setVisibility(View.GONE);
                tv_username.setText("等待商家接单");
                tv_arrive_time.setText("预计在30分钟内接单");
                tv_left_btn.setText("取消订单");
                tv_right_btn.setText("催一下");
                break;
            case 2:
                iv_avatar.setVisibility(View.GONE);
                tv_username.setText("商家派单中");
                tv_arrive_time.setText("等待商家派单");
                tv_left_btn.setText("取消订单");
                tv_right_btn.setText("催一下");
                break;
            case 3:
            case 4:
                iv_avatar.setVisibility(View.VISIBLE);
                Glide.with(this).load(item.icon).apply(GlideUtils.setCircleAvatar()).into(iv_avatar);
                tv_username.setText(item.orgName+"已接单");
                tv_arrive_time.setText("预计"+item.serviceTime+"到达");
                tv_left_btn.setText("取消订单");
                tv_right_btn.setVisibility(View.GONE);
                break;
            case 5://待确认
            case 6:
                Glide.with(this).load(item.icon).apply(GlideUtils.setCircleAvatar()).into(iv_avatar);
                iv_avatar.setVisibility(View.VISIBLE);
                tv_username.setText(item.orgName+"已接单");
                tv_arrive_time.setText("预计"+item.serviceTime+"到达");
                tv_left_btn.setText("取消订单");
                tv_right_btn.setText("确认订单");
                break;
            case 7://取消、删除
                iv_avatar.setVisibility(View.GONE);
                tv_username.setText("订单已取消");
                tv_arrive_time.setVisibility(View.GONE);
                tv_left_btn.setText("删除订单");
                tv_right_btn.setText("再来一单");
                tv_order_finish_time.setVisibility(View.VISIBLE);
                tv_order_finish_time_title.setVisibility(View.VISIBLE);
                break;
            case 9://评价
                iv_avatar.setVisibility(View.GONE);
                tv_username.setText("订单已完成");
                tv_arrive_time.setText("请您对本次服务做出评价");
                tv_left_btn.setText("删除订单");
                tv_right_btn.setText("去评价");
                tv_order_finish_time.setVisibility(View.VISIBLE);
                tv_order_finish_time_title.setVisibility(View.VISIBLE);
                break;
            case 10:
                iv_avatar.setVisibility(View.GONE);
                tv_username.setText("订单已完成");
                tv_arrive_time.setVisibility(View.GONE);
                tv_left_btn.setText("删除订单");
                tv_right_btn.setText("再来一单");
                tv_order_finish_time.setVisibility(View.VISIBLE);
                tv_order_finish_time_title.setVisibility(View.VISIBLE);
                break;
        }
    }
}
