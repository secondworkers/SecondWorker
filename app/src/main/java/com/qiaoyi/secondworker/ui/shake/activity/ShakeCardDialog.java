package com.qiaoyi.secondworker.ui.shake.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.WorkerBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.ui.center.center.*;
import com.qiaoyi.secondworker.ui.center.order.ConfirmOrderActivity;
import com.qiaoyi.secondworker.ui.recognition.VoiceIdentifyActivity;
import com.qiaoyi.secondworker.view.dialog.ServiceTypeDialog;

import cn.isif.alibs.utils.ToastUtils;

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
    private TextView tv_service_introduce,tv_xx;
    private ImageView iv_cancel;
    private Activity context;
    private WorkerBean workerBean;
    public interface ShakeCardListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshDialogUI(boolean isShake);
    }

    private ShakeCardListener listener;
    public ShakeCardDialog(@NonNull Activity context, WorkerBean workerBean,ShakeCardListener listener) {
        super(context);
        this.context = context;
        this.workerBean = workerBean;
        this.listener = listener;
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
        tv_xx = findViewById(R.id.tv_xx);
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
        tv_service.setText(workerBean.goodsName);
        if (!TextUtils.isEmpty(workerBean.introduction))
            tv_xx.setText(workerBean.introduction);
        if (!TextUtils.isEmpty(workerBean.workerName))
            tv_mg_name.setText(workerBean.workerName);
        tv_serviced_num.setText("已服务"+workerBean.counts+"人");
        ratingbar.setRating(Float.valueOf(workerBean.score+""));
        tv_rating.setText(workerBean.score+"分");
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
        listener.refreshDialogUI(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancel:
                dismiss();
                break;
            case R.id.tv_choose_it:
                if (null == AccountHandler.checkLogin()){
                    context.startActivity(new Intent(context,LoginActivity.class));
                }else {
                    ConfirmOrderActivity.startConfirmActivity(context,
                            workerBean.goodsName,
                            workerBean.workerId,
                            workerBean.unit,
                            workerBean.price,
                            workerBean.orgId,
                            workerBean.goodsId);
                }

                break;
            case R.id.tv_service_introduce:
                Intent intent = new Intent(context, ServiceIntroduceActivity.class);
                context.startActivity(intent);
                break;
        }
    }
    private void toRecognition() {
        if (AccountHandler.LOGIN_TYPE.NOBODY == AccountHandler.getUserState()) {
            //
        } else if (AccountHandler.LOGIN_TYPE.THIRD == AccountHandler.getUserState()) {
            ToastUtils.showShort("请先绑定手机号");
            BindMobileActivity.startBindMobileActivityForResult(context);
//
        } else if (AccountHandler.LOGIN_TYPE.MOBILE == AccountHandler.getUserState()) {

        }
    }
}
