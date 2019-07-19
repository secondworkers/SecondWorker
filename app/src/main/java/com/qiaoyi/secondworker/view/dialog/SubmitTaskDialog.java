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
import com.qiaoyi.secondworker.ui.center.tiktok.TiktokTaskDetailActivity;
import com.qiaoyi.secondworker.ui.shake.activity.ShakeActivity;


/**
 * Created on 2018/8/9
 *
 * @author Spirit
 */
public class SubmitTaskDialog extends Dialog implements View.OnClickListener {
    Context context;
    private TextView tv_goto;
    private String task_id;
    public interface ServiceChooseListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshDialogUI();
    }

    private ServiceChooseListener listener;
    public SubmitTaskDialog(@NonNull Context context,String task_id,ServiceChooseListener listener) {
        super(context, R.style.date_dialog_style);
        this.context = context;
        this.task_id = task_id;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        computeWeigth();
        setCanceledOnTouchOutside(false);
    }


    private void initViews() {
        setContentView(R.layout.dialog_submit);
        tv_goto = findViewById(R.id.tv_goto);
        tv_goto.setOnClickListener(this);
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
                Intent intent = new Intent(context, TiktokTaskDetailActivity.class);
                intent.putExtra("task_id",task_id);
                context.startActivity(intent);
                listener.refreshDialogUI();
                dismiss();
                break;
        }

    }
}
