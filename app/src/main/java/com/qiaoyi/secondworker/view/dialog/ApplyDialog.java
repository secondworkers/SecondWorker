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
import android.widget.TextView;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;

/**
 * Created on 2018/8/9
 *
 * @author Spirit
 */
public class ApplyDialog extends Dialog implements View.OnClickListener {
    Context context;
    private TextView tv_title2;
    private TextView tv_goto;
    private TextView iv_cancel;

    public ApplyDialog(@NonNull Context context) {
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
        setContentView(R.layout.activity_apply_become_secondworker_submit_audit);
        iv_cancel = findViewById(R.id.tv_i_see);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_i_see:
                context.startActivity(new Intent(context, MainActivity.class));
               dismiss();
                break;
        }

    }
}
