package com.qiaoyi.secondworker.ui.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.view.MyMapView;

/**
 * Created on 2019/4/24
 *
 * @author Spirit
 */

public class GetAddressActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_location;
    private EditText et_search;
    private MyMapView mapview;
    private RecyclerView rv_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getaddress);
        initView();
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);

    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_search = (EditText) findViewById(R.id.et_search);
        mapview = (MyMapView) findViewById(R.id.mapview);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);

        view_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:

                break;
            case R.id.tv_location:

                break;
        }
    }

    private void submit() {
        // validate
        String search = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(search)) {
            Toast.makeText(this, "请您输入服务地址", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
