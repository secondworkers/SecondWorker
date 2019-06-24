package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/6/22
 *  填写邀请码
 * @author Spirit
 */

public class FillInInvitationCodeActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout view_back;
    private EditText et_input;
    private TextView tv_btn,tv_title_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_invitation_code);
        VwUtils.fixScreen(this);
        initView();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        et_input = (EditText) findViewById(R.id.et_input);
        tv_btn = (TextView) findViewById(R.id.tv_btn);
        tv_title_text = (TextView) findViewById(R.id.tv_title_text);

        view_back.setOnClickListener(this);
        tv_btn.setOnClickListener(this);
        tv_title_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_btn:
                submit();
                break;case
            R.id.tv_title_text:
                startActivity(new Intent(this,InvitationActivity.class));
                break;
        }
    }

    private void submit() {
        // validate
        String input = et_input.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "您的邀请码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        ApiUserService.fillInvitation(input, new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response payload) {
                ToastUtils.showShort("绑定成功");
                finish();
            }
        });

    }
}
