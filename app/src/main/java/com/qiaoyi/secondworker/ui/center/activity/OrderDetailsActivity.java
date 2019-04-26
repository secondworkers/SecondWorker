package com.qiaoyi.secondworker.ui.center.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.view.CircularImageView;

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
    private TextView tv_order_pay_time;
    private TextView tv_left_btn;
    private TextView tv_right_btn;
    private String status;//order status

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetatil);
        initView();
        initData();
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
        tv_left_btn = (TextView) findViewById(R.id.tv_left_btn);
        tv_right_btn = (TextView) findViewById(R.id.tv_right_btn);
        tv_title_txt.setText("订单详情");
        view_back.setOnClickListener(this);
        tv_left_btn.setOnClickListener(this);
        tv_right_btn.setOnClickListener(this);
    }
    private void initData() {
        RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)	//加载成功之前占位图
                    .error(R.mipmap.ic_launcher)		//加载错误之后的错误图
                    .override(400,400)		//指定图片的尺寸
                    //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                    .fitCenter()
                    //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                    .centerCrop()
                    .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                    .skipMemoryCache(true)							//跳过内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.ALL)		//缓存所有版本的图像
                    .diskCacheStrategy(DiskCacheStrategy.NONE)		//跳过磁盘缓存
                    .diskCacheStrategy(DiskCacheStrategy.DATA)		//只缓存原来分辨率的图片
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);	//只缓存最终的图片

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_left_btn:

                break;
            case R.id.tv_right_btn:

                break;
        }
    }
}
