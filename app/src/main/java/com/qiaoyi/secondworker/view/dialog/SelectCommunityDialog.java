package com.qiaoyi.secondworker.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.R;


/**
 * Created on 2018/11/2
 *
 * @author Spirit
 */

public class SelectCommunityDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tv_cancel;
    private TextView tv_delete,tv_tips;
    private RelativeLayout rl_bg;
    private String tips;
    public interface DoneClickListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshUI();
    }
    private DoneClickListener dlistener;
    public interface CancelClickListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshUI();
    }
    private CancelClickListener clistener;
    public SelectCommunityDialog(@NonNull Context context,String tips, DoneClickListener dlistener,CancelClickListener clistener) {
        super(context, R.style.date_dialog_style);
        this.context = context;
        this.dlistener = dlistener;
        this.clistener = clistener;
        this.tips = tips;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete);
        computeWeigth();
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_delete = findViewById(R.id.tv_delete);
        tv_tips = findViewById(R.id.tv_tips);
        rl_bg = findViewById(R.id.rl_bg);
        tv_tips.setText(tips);
        rl_bg.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                clistener.refreshUI();
                dismiss();
                break;
            case R.id.tv_delete:
                dlistener.refreshUI();
                dismiss();
                break;
        }
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
}
