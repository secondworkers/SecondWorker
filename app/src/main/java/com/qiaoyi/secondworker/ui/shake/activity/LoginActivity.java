package com.qiaoyi.secondworker.ui.shake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.net.Contacts;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.LocationActivity;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import java.util.regex.Pattern;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/19
 *  登录
 * @author Spirit
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_getcode;
    private TextView tv_login;
    private ImageView iv_wechat;

    public static void startLoginActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
//        StatusBarUtil.setStatusBarColor(this,0xffffff);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_getcode = (TextView) findViewById(R.id.tv_getcode);
        tv_login = (TextView) findViewById(R.id.tv_login);
        iv_wechat = (ImageView) findViewById(R.id.iv_wechat);

        tv_getcode.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getcode:
                regexPhone();
                break;
            case R.id.tv_login:
                submit();
                break;
            case R.id.iv_wechat:
                ToastUtils.showShort("微信登录");
                break;
        }
    }

    /**
     * 验证手机号
     */
    private void regexPhone() {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || Pattern.matches(Contacts.REGEX_PHONE_NUM,phone)) {
            Toast.makeText(this, "请填写正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }else
            ApiUserService.sendSms(phone, new ServiceCallBack() {
                @Override
                public void failed(int code, String errorInfo, String source) {
                    ALog.e("失败");
                }

                @Override
                public void success(RespBean resp, Response payload) {
                    ALog.e("成功");
                }
            });

    }

    private void submit() {
        // validate
        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
    public void gotoLocationManger(){
       startActivity(new Intent(this,LocationActivity.class));
       finish();
    }
}
