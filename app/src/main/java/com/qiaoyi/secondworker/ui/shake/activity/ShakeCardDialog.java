package com.qiaoyi.secondworker.ui.shake.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.ui.center.activity.LoginActivity;
import com.qiaoyi.secondworker.ui.center.activity.SelectLocationActivity;

/**
 * Created on 2019/4/20
 * 摇一摇结果
 *
 * @author Spirit
 */
public class ShakeCardDialog extends Dialog implements View.OnClickListener {

    private TextView tv_service;
    private TextView tv_mg_name;
    private TextView tv_mg_distance;
    private RatingBar ratingbar;
    private TextView tv_rating;
    private TextView tv_serviced_num;
    private TextView tv_choose_it;
    private TextView tv_service_introduce;
    private ImageView iv_cancel;
    private Activity context;

    public ShakeCardDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        computeWeigth();
    }

    private void initView() {
        setContentView(R.layout.dialog_shake_card);
        tv_service = findViewById(R.id.tv_service);
        tv_mg_name = findViewById(R.id.tv_mg_name);
        tv_mg_distance = findViewById(R.id.tv_mg_distance);
        tv_rating = findViewById(R.id.tv_rating);
        tv_serviced_num = findViewById(R.id.tv_serviced_num);
        tv_choose_it = findViewById(R.id.tv_choose_it);
        tv_service_introduce = findViewById(R.id.tv_service_introduce);
        ratingbar = findViewById(R.id.ratingbar);
        iv_cancel = findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);
        tv_choose_it.setOnClickListener(this);
        tv_service_introduce.setOnClickListener(this);
    }

    private void initData() {
    }

    private void computeWeigth() {
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = 0;
        window.setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancel:
                dismiss();
                break;
            case R.id.tv_choose_it:
                if (null == AccountHandler.checkLogin()){
                    LoginActivity.startLoginActivity(context,1001);
                }else {
                    context.startActivity(new Intent(context,SelectLocationActivity.class));
                    dismiss();
                }
                break;
            case R.id.tv_service_introduce:
                Intent intent = new Intent(context, ServiceIntroduceActivity.class);
                context.startActivity(intent);
                break;
        }
    }

}
