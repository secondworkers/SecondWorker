package com.qiaoyi.secondworker.ui.center.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.WrapCommentBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created by Spirit on 2019/5/23.
 */

public class PostCommentActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_user_photo;
    private TextView tv_user_name;
    private TextView tv_service_nane;
    private RatingBar rating_bar;
    private EditText et_import;
    private TextView tv_font_bottom;
    private String serviceItem;
    private String orderId;
    private float sore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        Intent intent = getIntent();
        serviceItem = intent.getStringExtra("serviceItem");
        orderId = intent.getStringExtra("orderId");
        setContentView(R.layout.activity_evaluate);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        iv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_service_nane = (TextView) findViewById(R.id.tv_service_nane);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        et_import = (EditText) findViewById(R.id.et_import);
        tv_font_bottom = (TextView) findViewById(R.id.tv_font_bottom);

        view_back.setOnClickListener(this);
        tv_font_bottom.setOnClickListener(this);
        tv_service_nane.setText(serviceItem);
        tv_title_txt.setText("评论");
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                sore = rating;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_font_bottom:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String comment = et_import.getText().toString().trim();
        if (TextUtils.isEmpty(comment)){
            Toast.makeText(this, "您的认可是我们的动力，请留下宝贵意见", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        ApiUserService.postComment(orderId, comment, String.valueOf(sore), new ServiceCallBack<WrapCommentBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapCommentBean> payload) {
                ToastUtils.showShort("评价成功");
                finish();
            }
        });

    }
}
