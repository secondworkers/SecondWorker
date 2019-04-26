package com.qiaoyi.secondworker.ui.center.activity;

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
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

/**
 * Created on 2019/4/23
 *  填写地址
 *      从编辑进来有删除按钮 edit , 从添加进来没有删除按钮--add
 * @author Spirit
 */

public class LocationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private View view_title_line;
    private TextView tv_location;
    private EditText et_location_more;
    private TextView tv_contact;
    private TextView tv_phone;
    private TextView tv_delete;
    private TextView tv_save;
    private LinearLayout ll_delete_save;
    private TextView tv_save1;
    private String from;

    public static void startLocationActivity(Activity activity,String from){
        Intent intent = new Intent(activity, LocationActivity.class);
        intent.putExtra("from",from);
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
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        view_title_line = (View) findViewById(R.id.view_title_line);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_location_more = (EditText) findViewById(R.id.et_location_more);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_save = (TextView) findViewById(R.id.tv_save);
        ll_delete_save = (LinearLayout) findViewById(R.id.ll_delete_save);
        tv_save1 = (TextView) findViewById(R.id.tv_save1);

        view_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_title_txt.setText("填写地址");
    }
    private void initData() {
        if (!TextUtils.isEmpty(from)){
            if (from.equals("add")){
                ll_delete_save.setVisibility(View.GONE);
                tv_save1.setVisibility(View.VISIBLE);
            }else if (from.equals("from")){
                ll_delete_save.setVisibility(View.VISIBLE);
                tv_save1.setVisibility(View.GONE);
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
                break;
        }
    }

    private void submit() {
        // validate
        String more = et_location_more.getText().toString().trim();
        if (TextUtils.isEmpty(more)) {
            Toast.makeText(this, "如2号楼3单元602室", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
