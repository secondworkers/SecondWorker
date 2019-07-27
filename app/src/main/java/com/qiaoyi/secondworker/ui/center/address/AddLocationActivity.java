package com.qiaoyi.secondworker.ui.center.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.LocationBean;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.bean.OrderConfirmEvent;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/23
 *  填写地址
 *      从编辑进来有删除按钮 edit , 从添加进来没有删除按钮--add
 * @author Spirit
 */

public class AddLocationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private View view_title_line;
    private TextView tv_location;
    private EditText et_location_more,tv_contact,tv_phone;
    private TextView tv_delete;
    private TextView tv_save;
    private LinearLayout ll_delete_save;
    private TextView tv_save1;
    private String from,address_id,user_name,address_title,address_phone,address_msg;
    private double lat;
    private double lng;

    public static void startLocationActivity(Activity activity,String from,
                                             double lat,
                                             double lng,
                                             String user_name,
                                             String address_title,
                                             String address_phone,
                                             String address_msg,
                                             String address_id){
        Intent intent = new Intent(activity, AddLocationActivity.class);
        intent.putExtra("from",from);
        intent.putExtra("user_name",user_name);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        intent.putExtra("address_title",address_title);
        intent.putExtra("address_phone",address_phone);
        intent.putExtra("address_msg",address_msg);
        intent.putExtra("address_id",address_id);
        activity.startActivity(intent);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_location);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        user_name = intent.getStringExtra("user_name");
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);
        address_title = intent.getStringExtra("address_title");
        address_phone = intent.getStringExtra("address_phone");
        address_msg = intent.getStringExtra("address_msg");
        address_id = intent.getStringExtra("address_id");

        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_title_line = (View) findViewById(R.id.view_title_line);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_location_more = (EditText) findViewById(R.id.et_location_more);
        tv_contact = (EditText) findViewById(R.id.tv_contact);
        tv_phone = (EditText) findViewById(R.id.tv_phone);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_save = (TextView) findViewById(R.id.tv_save);
        ll_delete_save = (LinearLayout) findViewById(R.id.ll_delete_save);
        tv_save1 = (TextView) findViewById(R.id.tv_save1);

        view_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        ll_delete_save.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_save1.setOnClickListener(this);
        tv_title_txt.setText("填写地址");
    }
    private void initData() {
        if (!TextUtils.isEmpty(from)){
            if (from.equals("add")){
                ll_delete_save.setVisibility(View.GONE);
                tv_save1.setVisibility(View.VISIBLE);
            }else if (from.equals("edit")){
                ll_delete_save.setVisibility(View.VISIBLE);
                tv_save1.setVisibility(View.GONE);
                tv_location.setText(address_title);
                tv_contact.setText(user_name);
                tv_phone.setText(address_phone);
                et_location_more.setText(address_msg);

            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_location:
                startActivity(new Intent(this,GetAddressActivity.class));
                break;
            case R.id.tv_save:
            case R.id.tv_save1:
                //TODO request api
                submit();
                break;
            case R.id.ll_delete_save:
                deleteAddress();
                break;
        }
    }


    private void submit() {
        // validate
        address_msg = et_location_more.getText().toString().trim();
        String contact = tv_contact.getText().toString().trim();
        String phone = tv_phone.getText().toString().trim();
        if (TextUtils.isEmpty(address_msg)||TextUtils.isEmpty(contact)||TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }else {
            ApiUserService.updateAddressList(address_id, AccountHandler.getUserId(),lat,lng,
                    contact,address_title,
                    phone,
                    address_msg,
                    new ServiceCallBack() {
                @Override
                public void failed(String code, String errorInfo, String source) {
                    ToastUtils.showShort(errorInfo);
                }

                @Override
                public void success(RespBean resp, Response payload) {
                    if (TextUtils.isEmpty(address_id)){//如果aid为空 为增加address 不为空为修改，修改的话直接选中 返回订单确认
                        EventBus.getDefault().post(new MessageEvent("add_success"));
                    }else {
                        EventBus.getDefault().post(new OrderConfirmEvent(address_title+" "+address_msg,
                                user_name+" "+address_phone,
                                address_id));
                    }
                    finish();
                }
            });
        }
    }
    private void deleteAddress() {
        ApiUserService.delAddress(address_id,"2", new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response payload) {
                EventBus.getDefault().post(new MessageEvent("add_success"));
                //
                finish();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onLocationSelect(LocationBean location){
        lat = location.getLat();
        lng = location.getLng();
        String address_msg = location.getAddress_msg();
        address_title = location.getAddress_msg();
        ALog.e("lat="+ lat +",lng="+ lng +"\naddress_msg="+address_msg+"address_title="+address_title);
        tv_location.setText(address_msg);
        et_location_more.setText("");
        et_location_more.setHint("请输入详细地址");
        //重新根据请求数据
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
