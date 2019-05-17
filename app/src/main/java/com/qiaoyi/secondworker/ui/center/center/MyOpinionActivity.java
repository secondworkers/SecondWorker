package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/7
 *  意见与反馈
 * @author Spirit
 */

public class MyOpinionActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_service_hotline;
    private TextView tv_contact_mail;
    private EditText et_input_box1;
    private EditText et_input_box2;
    private Button post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_opinion);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_service_hotline = (TextView) findViewById(R.id.tv_service_hotline);
        tv_contact_mail = (TextView) findViewById(R.id.tv_contact_mail);
        et_input_box1 = (EditText) findViewById(R.id.et_input_box1);
        et_input_box2 = (EditText) findViewById(R.id.et_input_box2);
        post = (Button) findViewById(R.id.post);

        view_back.setOnClickListener(this);
        post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.post:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String box1 = et_input_box1.getText().toString().trim();
        if (TextUtils.isEmpty(box1)) {
            Toast.makeText(this, "请简要说明您所遇到的问题", Toast.LENGTH_SHORT).show();
            return;
        }

        String box2 = et_input_box2.getText().toString().trim();
        if (TextUtils.isEmpty(box2)) {
            Toast.makeText(this, "如需得到反馈请留下您的联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        ToastUtils.showLong("我们已经收到您的意见");
        // TODO validate success, do something

        finish();
    }
}
