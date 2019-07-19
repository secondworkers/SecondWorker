package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.BaseRequirementBean;
import com.qiaoyi.secondworker.bean.RequirementBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.pay.PayHandler;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/7/2
 *
 * @author Spirit
 */

public class WaitingReceiveActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_circle;
    private TextView tv_waiting_matching;
    private TextView tv_location;
    private TextView tv_phone;
    private TextView tv_service_type;
    private TextView tv_time;
    private TextView tv_evaluate;
    private TextView tv_hint;
    private TextView tv_cancel_order;
    private String id;
    private String status;
    private RequirementBean body;
    private Animation rotate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_receive);
        VwUtils.fixScreen(this);
        id = getIntent().getStringExtra("id");
        initView();
        request();
        getPulling();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_circle = (ImageView) findViewById(R.id.iv_circle);
        tv_waiting_matching = (TextView) findViewById(R.id.tv_waiting_matching);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_cancel_order = (TextView) findViewById(R.id.tv_cancel_order);

        view_back.setOnClickListener(this);
        tv_cancel_order.setOnClickListener(this);
        circleAnimation();
    }
    private void circleAnimation() {
        rotate = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin); //设置插值器
        rotate.setDuration(3000);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        rotate.setStartOffset(1000);//执行前的等待时间  单位ms
        iv_circle.setAnimation(rotate);
    }

    private void request() {
        ApiUserService.getRequirementDetail(id, new ServiceCallBack<BaseRequirementBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<BaseRequirementBean> payload) {
                body = payload.body().result;
                status = body.status;
                initData();
            }
        });
    }

    private void initData() {
        if ("1".equals(status)){
            tv_title_txt.setText("等待接单");
            tv_waiting_matching.setText("匹配中");
            tv_evaluate.setText("估价中 — — 元");
            tv_hint.setVisibility(View.INVISIBLE);
            tv_cancel_order.setText("取消订单");
            tv_cancel_order.setBackground(getResources().getDrawable(R.drawable.shape_kuangt_grey));
            iv_circle.startAnimation(rotate);
            tv_cancel_order.setTextColor(getResources().getColor(R.color.text_grey));
        } else {
            iv_circle.clearAnimation();
            tv_title_txt.setText("等待支付");
            tv_waiting_matching.setText("匹配成功");
            tv_evaluate.setText(Html.fromHtml("<font>"
                    + "最低价"
                    + "</font><font> <big>"
                    + body.actualPay
                    + "</big></font><font>"
                            + "元"
                            + "</font>"));
            tv_hint.setVisibility(View.VISIBLE);
            tv_cancel_order.setText("确认支付");
            tv_cancel_order.setBackground(getResources().getDrawable(R.drawable.shape_wallet_btn_blue));
            tv_cancel_order.setTextColor(getResources().getColor(R.color.text_white));

            mHandler.removeCallbacks(runnable);
        }
        tv_location.setText(body.addressName);
        tv_phone.setText(body.phone);
        tv_service_type.setText(body.serviceType);
        tv_time.setText(body.atime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_cancel_order:
                if ("1".equals(status)){
                    ApiUserService.updateRequirementList(id, "2", new ServiceCallBack() {
                        @Override
                        public void failed(String code, String errorInfo, String source) {

                        }

                        @Override
                        public void success(RespBean resp, Response payload) {
                            ToastUtils.showShort("订单已取消");
                            finish();
                        }
                    });
                }else if ("3".equals(status)){
                    PayHandler.onRequest(this,body.orderId,body.actualPay,0.0,body.serviceType,1,"");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iv_circle.clearAnimation();
        mHandler.removeCallbacks(runnable);
    }
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private void getPulling() {
        runnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 3 * 1000);
                //需要做轮询的方法
                request();
            }
        };
        mHandler.postDelayed(runnable, 3 * 1000);
    }

}
