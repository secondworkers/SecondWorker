package com.qiaoyi.secondworker.ui.center.center;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.BuildConfig;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * create on 2019/4/25
 * ling
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_font_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("关于我们");
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_font_bottom = (TextView) findViewById(R.id.tv_font_bottom);
        tv_font_bottom.setText("版本号：V"+ BuildConfig.VERSION_NAME);
        view_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }
}
