package com.qiaoyi.secondworker.ui.center.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.OrderBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.order.OrderDetailsActivity;
import com.qiaoyi.secondworker.ui.center.order.PrePayActivity;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/9
 *
 * @author Spirit
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {
    Activity context;
    private TextView tv_status;
    private ImageView iv_avatar,iv_service_icon;
    private TextView tv_worker_name;
    private TextView tv_count_time;
    private TextView tv_btn1;
    private TextView tv_btn2;
    private CountDownTimer countDownTimer;

    public OrderListAdapter(int item_order, Activity context) {
        super(item_order);
        this.context = context;
    }

    /**
     * all -- qb
     * 0：待付款 -- dfk
     * 1：等待商家接单 2：派单中 3：已接单 4.工人正在路上 //待服务 -- dfw
     * 5.工人正在服务 6.完成服务  //待确认 -- dqr
     * 7.取消订单 8.删除订单
     * 9.待评价 -- dpj
     * 10.已完成
     *
     * 0.待支付  -- dfk
     * 1.待接单2.待服务  //待服务 -- dfw
     * 3.待评价4.已评价  //已完成 -- ywc
     * 5.已取消   //已取消-- 已取消
     * 6.删除订单
     *
     *
     */
    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        tv_status = helper.getView(R.id.tv_order_status);
        iv_avatar = helper.getView(R.id.iv_avatar);
        iv_service_icon = helper.getView(R.id.iv_service_icon);
        tv_worker_name = helper.getView(R.id.tv_worker_name);
        tv_count_time = helper.getView(R.id.tv_count_time);
        tv_btn1 = helper.getView(R.id.tv_btn1);
        tv_btn2 = helper.getView(R.id.tv_btn2);
        tv_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLeftClick(item,helper.getPosition());
            }
        });
        tv_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRightClick(item,helper.getPosition());
            }
        });
        changeByStatus(item,helper.getPosition());
        tv_worker_name.setText(item.orgName);
        RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.avatar)
             .error(R.mipmap.avatar)
            .fitCenter()
            .centerCrop()
            .circleCrop();
        Glide.with(context).load(item.icon).apply(options).into(iv_avatar);
        Glide.with(context).load(item.goodsPhoto).apply(GlideUtils.setRoundTransform(context,1)).into(iv_service_icon);
        helper.setText(R.id.tv_service_type,item.goodsName);
        helper.setText(R.id.tv_service_time,item.serviceTime);
        helper.setText(R.id.tv_service_price,item.price+item.unit);
        helper.setText(R.id.tv_service_num,"X"+item.payCount);
        helper.setText(R.id.tv_create_time,item.creatime);
        helper.setText(R.id.tv_total_price, Html.fromHtml("<font color='#666666'>总价 "
                + "</font><font color='#ff0000'>"
                + item.actualPay
                + "元</font>"));
        helper.getView(R.id.rl_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.gotoOrderDetails(context,item.orderid);
            }
        });
    }

    private void changeByStatus(OrderBean item,int pos) {
        switch (item.status){
            case 0:
                long timStamp = item.ctime + 90000 - item.systemTime;
                tv_status.setText("待付款");
                tv_btn1.setText("取消订单");
                tv_btn2.setText("去支付");
                countDownTimer(item,timStamp,pos);
                tv_count_time.setVisibility(View.VISIBLE);
                break;
            case 1://待服务
            case 2:
                tv_status.setText("待服务");
                tv_btn1.setText("再来一单");
                tv_btn2.setText("确认完成");
                tv_count_time.setVisibility(View.GONE);
                break;
            case 3://评价
                tv_status.setText("待评价");
                tv_btn1.setVisibility(View.VISIBLE);
                tv_btn2.setVisibility(View.VISIBLE);
                tv_btn1.setText("再来一单");
                tv_btn2.setText("去评价");
                tv_count_time.setVisibility(View.GONE);
                break;
            case 4:
                tv_status.setText("已完成");
                tv_btn1.setVisibility(View.VISIBLE);
                tv_btn2.setVisibility(View.VISIBLE);
                tv_btn1.setText("删除订单");
                tv_btn2.setText("再来一单");
                tv_count_time.setVisibility(View.GONE);
                break;
            case 5://取消、删除
            case 6:
                tv_status.setText("已取消");
                tv_btn1.setVisibility(View.VISIBLE);
                tv_btn2.setVisibility(View.VISIBLE);
                tv_btn1.setText("删除订单");
                tv_btn2.setText("再来一单");
                tv_count_time.setVisibility(View.GONE);
                break;
        }
    }

    private void btnLeftClick(OrderBean item,int pos) {
        switch (item.status){
            case 0:
                cancelOrDelOrder(item,pos,5);
                break;
            case 1://待服务
            case 2:
            case 3://待评价
               context.startActivity(new Intent(context, MainActivity.class));
               context.finish();
                break;
            case 4://已评价
            case 5:
                cancelOrDelOrder(item,pos,6);
                break;
        }
    }
    private void btnRightClick(OrderBean item,int pos) {
        switch (item.status){
            case 0:
               PrePayActivity.startPrePayActivity(context,item.orderid,item.goodsName,item.actualPay);
               context.finish();
                break;
            case 1://待服务
            case 2:
                cancelOrDelOrder(item,pos,3);
                break;
            case 3://待评价
                OrderDetailsActivity.gotoOrderDetails(context,item.orderid);
                context.finish();
                break;
            case 5://取消、删除
                context.startActivity(new Intent(context, MainActivity.class));
                context.finish();
                break;
        }
    }

    private void cancelOrDelOrder(OrderBean bean,int pos,int status) {
        ApiUserService.cancelAndDelOrder(bean.orderid, status, new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }
            @Override
            public void success(RespBean resp, Response payload) {
                if (status == 6)//删除订单
                    remove(pos);
                else if (status == 5 ){//取消订单
                    bean.setStatus(5);
                    notifyItemChanged(pos);
                    context.finish();
                }else if (status == 3){
                    bean.setStatus(3);
                    notifyItemChanged(pos);
                }
            }
        });
    }
    void countDownTimer(OrderBean item,long timStamp,int pos){

        //请求取消订单接口
        countDownTimer = new CountDownTimer(timStamp, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_count_time.setText("剩余支付时间"+VwUtils.longToTimeStr(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                //请求取消订单接口
                cancelOrDelOrder(item,pos,5);
            }
        };
        countDownTimer.start();
    }
}
