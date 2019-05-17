package com.qiaoyi.secondworker.ui.center.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;

import org.json.JSONException;
import org.json.JSONObject;

import cn.isif.alibs.utils.ToastUtils;

public class BindMobileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back_button;
    private TextView tv_title;
    EditText et_mobile_number;
    EditText et_verify_code;
    TextView tv_get_verify_code;
    Button bt_band;
    String mobileNumber;
    String inputCode;
    private TextView tv_title_txt;
    private RelativeLayout view_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        initView();
    }

    void initView() {

        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_verify_code = findViewById(R.id.et_verify_code);
        tv_get_verify_code = findViewById(R.id.tv_get_verify_code);
        bt_band = findViewById(R.id.bt_band);
        tv_get_verify_code.setOnClickListener(this);
        bt_band.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("手机绑定");
        et_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入文字中的状态，count是输入字符数
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入文本之前的状态
            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入文字后的状态
                if (checkInputCode()) {
                    iv_back_button.setEnabled(true);
                } else {
                    iv_back_button.setEnabled(false);
                }
            }
        });
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setOnClickListener(this);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_get_verify_code:
                tv_get_verify_code.setClickable(false);
                if (checkMobileNumber()) {
                    //请求手机验证码
                    ApiUserService.sendSms(mobileNumber, new ServiceCallBack() {

                        @Override
                        public void failed(String code, String errorInfo, String source) {
                            //发送失败-提示
                            ToastUtils.showShort(errorInfo);
                            tv_get_verify_code.setClickable(true);
                        }

                        @Override
                        public void success(RespBean resp, Response payload) {
                            //发送成功-提示
                            ToastUtils.showShort("发送成功");
                            new Thread(new ScheduledRunnable()).start();
                        }
                    });
                } else {
                    //手机号码输入错误
                    tv_get_verify_code.setClickable(true);
                    ToastUtils.showShort("手机号码输入错误");
                }
                break;
            case R.id.bt_band:
                if (checkMobileNumber() && checkInputCode()) {
                    ApiUserService.bindThird(mobileNumber, inputCode, new ServiceCallBack() {
                        @Override
                        public void failed(String code, String errorInfo, String source) {

                        }

                        @Override
                        public void success(RespBean resp, Response payload) {
                            try {
                                JSONObject jsonObject = new JSONObject(payload.body().toString());
                                JSONObject rep = jsonObject.getJSONObject("rspData");
                                JSONObject jsonObject1 = new JSONObject(rep.toString());
                                JSONObject result = jsonObject1.getJSONObject("result");
                                String id = result.getString("uid");
                                if (!TextUtils.isEmpty(id)) {
                                    AccountHandler.saveLoginInLocal(result.toString());
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    ToastUtils.showShort("未知异常");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setResult(RESULT_OK);
                            finish();

                        }
                    });
                }
                break;
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what > 0) {
                tv_get_verify_code.setClickable(false);
                tv_get_verify_code.setText(what + "s");
            } else {
                tv_get_verify_code.setClickable(true);
                tv_get_verify_code.setText("获取验证码");
            }
        }
    };

    class ScheduledRunnable implements Runnable {
        boolean stop = false;
        int step = 60;

        @Override
        public void run() {
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

    //检查用户输入的手机号
    boolean checkMobileNumber() {
        mobileNumber = et_mobile_number.getText().toString();
        return !TextUtils.isEmpty(mobileNumber) && et_mobile_number.length() == 11;
    }

    boolean checkInputCode() {
        inputCode = et_verify_code.getText().toString();
        return !TextUtils.isEmpty(inputCode) && inputCode.length() == 6;
    }

    public static void startBindMobileActivityForResult(Activity context) {
        Intent intent = new Intent(context, BindMobileActivity.class);
        context.startActivity(intent);
    }
}
