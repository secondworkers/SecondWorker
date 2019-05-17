package com.qiaoyi.secondworker.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.ui.shake.activity.ShakeActivity;


/**
 * Created on 2018/8/9
 *
 * @author Spirit
 */
public class ShakeDialog extends Dialog implements View.OnClickListener {
    Context context;
    private TextView tv_title2;
    private TextView tv_goto;
    private ImageView iv_cancel;

    public ShakeDialog(@NonNull Context context) {
        super(context, R.style.date_dialog_style);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        computeWeigth();
        setCanceledOnTouchOutside(true);
        initData();
    }


    private void initViews() {
        setContentView(R.layout.dialog_new_user);
        tv_title2 = findViewById(R.id.tv_title2);
        tv_goto = findViewById(R.id.tv_goto);
        tv_goto.setOnClickListener(this);
        iv_cancel = findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);
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
            case R.id.tv_goto:
                context.startActivity(new Intent(context, ShakeActivity.class));
                dismiss();
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
        }

    }
}
