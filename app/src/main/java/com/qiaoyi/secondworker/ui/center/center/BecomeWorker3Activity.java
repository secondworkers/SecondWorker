package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.LocationBean;
import com.qiaoyi.secondworker.bean.WrapRequirementBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.address.CityPickerActivity;
import com.qiaoyi.secondworker.ui.center.address.GetAddressActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.ApplyDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.isif.alibs.utils.ALog;

/**
 * Created on 2019/5/15
 *
 * @author Spirit
 */

public class BecomeWorker3Activity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_right;
    private TextView et_select_city;
    private TextView et_select_enter_area;
    private EditText et_input_phone_number;
    private EditText et_input_verification_code,et_address;
    private TextView tv_apply_immediately,tv_get_code;
    private double city_lat;
    private double city_lng;
    private String name;
    private String number;
    private ArrayList<String> imgLists;
    private String city_code;
    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        imgLists = intent.getStringArrayListExtra("imgList");
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_apply_become_secondworker_fill_service_area);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("申请成为秒工人");
        view_right = (RelativeLayout) findViewById(R.id.view_back);
        et_select_city = (TextView) findViewById(R.id.et_select_city);
        et_select_enter_area = (TextView) findViewById(R.id.et_select_enter_area);
        et_input_phone_number = (EditText) findViewById(R.id.et_input_phone_number);
        et_input_verification_code = (EditText) findViewById(R.id.et_input_verification_code);
        et_address = (EditText) findViewById(R.id.et_address);
        tv_apply_immediately = (TextView) findViewById(R.id.tv_apply_immediately);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);

        view_right.setOnClickListener(this);
        et_select_city.setOnClickListener(this);
        et_select_enter_area.setOnClickListener(this);
        tv_apply_immediately.setOnClickListener(this);
        tv_get_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_right:
                    finish();
                break;
//            case R.id.et_select_city:
//                Intent intent = new Intent(this, CityPickerActivity.class);
//                intent.putExtra("location_city",city);
//                startActivityForResult(intent,3001);
//                break;
            case R.id.tv_get_code:
                tv_get_code.setClickable(false);
                phone = et_input_phone_number.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)){
                    ApiUserService.sendSms(phone, new ServiceCallBack() {
                        @Override
                        public void failed(String code, String errorInfo, String source) {
                            tv_get_code.setClickable(true);
                        }

                        @Override
                        public void success(RespBean resp, Response payload) {
                            new Thread(new ScheduledRunnable()).start();

                        }
                    });
                }else {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.et_select_enter_area:
                startActivity(new Intent(this, GetAddressActivity.class));
                break;
            case R.id.tv_apply_immediately:
                submit();
                break;
        }
    }
    Handler handler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what > 0) {
                tv_get_code.setClickable(false);
                tv_get_code.setText(what + "s");
            }else {
                tv_get_code.setClickable(true);
                tv_get_code.setText("获取验证码");
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onLocationSelect(LocationBean location){
        double lat = location.getLat();
        double lng = location.getLng();
        String address_msg = location.getAddress_msg();
        String address_title = location.getAddress_title();
        ALog.e("lat="+lat+",lng="+lng+"\naddress_msg="+address_msg+"address_title="+address_title);
        et_select_enter_area.setText(address_msg+address_title);
    }
    private void submit() {
        // validate
        phone = et_input_phone_number.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String verificaCode = et_input_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(verificaCode)) {
            Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something
        ApiUserService.applyWorker(name, number,
                imgLists.get(0), imgLists.get(1), imgLists.get(2), imgLists.get(3),
                city, address, city_lng, city_lat, phone,
                "0", new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response payload) {
                new ApplyDialog(BecomeWorker3Activity.this).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        et_select_city.setText(data.getStringExtra("select_city"));
        city_lat = data.getDoubleExtra("city_lat", 0.0);
        city_lng = data.getDoubleExtra("city_lng", 0.0);
        city_code = data.getStringExtra("city_code");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
