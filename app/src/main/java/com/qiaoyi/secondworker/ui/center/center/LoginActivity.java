package com.qiaoyi.secondworker.ui.center.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.address.MyLocationActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;
import cn.isif.umlibs.UmengUtil;

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
    private String phone;

    public static void startLoginActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
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
        tv_getcode.setClickable(true);
        tv_login.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        UMShareConfig config = new UMShareConfig();
        switch (v.getId()) {
            case R.id.tv_getcode:
                regexPhone();
                tv_getcode.setClickable(false);
                break;
            case R.id.tv_login:
                submit();
                break;
            case R.id.iv_wechat:
                config.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(this).setShareConfig(config);
                UmengUtil.authWeiXin(this, new AuthListener());
                break;
        }
    }
    class AuthListener implements UMAuthListener {
        @Override public void onStart(SHARE_MEDIA share_media) {
            ALog.e("onStart " + "授权开始");
        }

        @Override public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            ALog.e("onComplete " + "授权完成");
            String uid = map.get("uid");
            String openid = map.get("openid");//微博没有
            String unionid = map.get("unionid");//微博没有
            String phone = map.get("phone");
            String access_token = map.get("access_token");
            String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
            String expires_in = map.get("expires_in");
            String name = map.get("name");
            String gender = map.get("gender");
            String iconurl = map.get("iconurl");
            ApiUserService.loginThird(openid, share_media == SHARE_MEDIA.QQ ? "2" : "1", name, iconurl,
                    new ServiceCallBack() {
                        @Override
                        public void failed(String code, String errorInfo, String source) {
                            ToastUtils.showLong(errorInfo);
                        }

                        @Override
                        public void success(RespBean resp, Response payload) {
                            try {
                                JSONObject jsonObject = new JSONObject(payload.body().toString());
                                JSONObject rep = jsonObject.getJSONObject("rspData");
                                loginSuccess(rep.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        @Override public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ALog.d("onError " + "授权失败");
            ToastUtils.showLong("onError " + "授权失败");
        }

        @Override public void onCancel(SHARE_MEDIA share_media, int i) {
            ALog.d("onCancel " + "授权取消");
            ToastUtils.showLong("onCancel " + "授权取消");
        }
    }
    /**
     * 验证手机号
     */
    private void regexPhone() {
        phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            Toast.makeText(this, "请填写正确的手机号", Toast.LENGTH_SHORT).show();
            tv_getcode.setClickable(true);
            return;
        }else{
            ApiUserService.sendSms(phone, new ServiceCallBack() {
                @Override
                public void failed(String code, String errorInfo, String source) {
                    ToastUtils.showShort(errorInfo);
                    tv_getcode.setClickable(true);
                    ALog.e("失败");
                }

                @Override
                public void success(RespBean resp, Response payload) {
                    Object body = payload.body();
                    ALog.e("成功");
                    new Thread(new ScheduledRunnable()).start();
                }
            });

        }

    }
    Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what > 0) {
                tv_getcode.setClickable(false);
                tv_getcode.setText(what + "s");
            }else {
                tv_getcode.setClickable(true);
                tv_getcode.setText("获取验证码");
            }
        }
    };

    class ScheduledRunnable implements Runnable {
        boolean stop = false;
        int step = 60;

        @Override public void run() {
            while (!stop) {
                try {
                    Thread.sleep(1_000);
                    --step;
                    handler.sendEmptyMessage(step);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (step < 0) {
                    stop = true;
                }
            }
            handler.sendEmptyMessage(step);
        }
    }
    private void submit() {
        // validate
        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        ApiUserService.login(phone, code, new ServiceCallBack() {
            @Override public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override public void success(RespBean resp, Response payload) {
                String response = payload.body().toString();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONObject rspData = jsonObject.getJSONObject("rspData");
                    loginSuccess(rspData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    /**
     * 登录、三方登录成功
     * @param response
     */
    private void loginSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            String id = result.getString("uid");
            if (!TextUtils.isEmpty(id)) {
                AccountHandler.saveLoginInLocal(result.toString());
                EventBus.getDefault().post(new MessageEvent("SUCCESS"));
//                bindPush(id);
                finish();
            } else {
                ToastUtils.showShort("未知异常");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  void gotoLocationManger(){
       startActivity(new Intent(this,MyLocationActivity.class));
       finish();
    }
}
