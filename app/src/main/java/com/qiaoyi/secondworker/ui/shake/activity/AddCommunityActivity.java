package com.qiaoyi.secondworker.ui.shake.activity;

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
import com.qiaoyi.secondworker.ui.center.address.CityPickerActivity;
import com.qiaoyi.secondworker.ui.center.address.MyLocationActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.SelectCommunityDialog;

import cn.isif.alibs.utils.SharePreferenceUtils;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/8/7
 *
 * @author Spirit
 */

public class AddCommunityActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_city_name;
    private EditText et_communityname;
    private TextView tv_done;
    private String city_code;
    private String province_code;
    private String province;
    private String communityname;
    private String select_city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_community);
        VwUtils.fixScreen(this);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_city_name = (TextView) findViewById(R.id.tv_city_name);
        et_communityname = (EditText) findViewById(R.id.et_communityname);
        tv_done = (TextView) findViewById(R.id.tv_done);

        view_back.setOnClickListener(this);
        tv_city_name.setOnClickListener(this);
        tv_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_city_name:
                Intent intent = new Intent(this, CityPickerActivity.class);
                intent.putExtra("location_city",city);
                startActivityForResult(intent,4001);
                break;
            case R.id.tv_done:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        communityname = et_communityname.getText().toString().trim();
        if (TextUtils.isEmpty(communityname)) {
            Toast.makeText(this, "请手动输入您所在社区/村庄/小区名称", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        requestData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode){
            if (requestCode == 4001){
                select_city = data.getStringExtra("select_city");
                tv_city_name.setText(select_city);
//                city_code = data.getStringExtra("city_code");
                province_code = data.getStringExtra("province_code");
                province = data.getStringExtra("province");
            }
        }
    }

    private void requestData() {
        new SelectCommunityDialog(this, "请确认添加小区正确并选择该 小区，选择之后无法更改！",new SelectCommunityDialog.DoneClickListener() {
            @Override
            public void refreshUI() {
                ApiUserService.chooseShequ("",communityname,cityCode,select_city,province_code,province, new ServiceCallBack() {
                    @Override
                    public void failed(String code, String errorInfo, String source) {
                        ToastUtils.showShort(errorInfo);
                        finish();
                    }

                    @Override
                    public void success(RespBean resp, Response payload) {
                        String body = (String)payload.body();
                        SharePreferenceUtils.write("sqID","sqID",body);
                        startActivity(new Intent(AddCommunityActivity.this,OnePlanActivity.class));
                        finish();
                    }
                });
            }
        }, new SelectCommunityDialog.CancelClickListener() {
            @Override
            public void refreshUI() {
                requestData();
            }
        }).show();
    }
}
