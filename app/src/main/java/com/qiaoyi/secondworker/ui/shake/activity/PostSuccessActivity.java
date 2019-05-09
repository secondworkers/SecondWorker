package com.qiaoyi.secondworker.ui.shake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/4/24
 *  发布成功
 * @author Spirit
 */

public class PostSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private ImageView iv_title_back;
    private RelativeLayout view_back;
    private TextView tv_title_more;
    private View view_title_line;
    private ImageView imageView;
    private TextView tv_success;
    private TextView tv_share;
    private TextView tv_see_details;
    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_success);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        if (from.equals("pay")){//支付成功

        }else if (from.equals("post")){//发布成功

        }
        initView();
        initData();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_title_more = (TextView) findViewById(R.id.tv_title_more);
        view_title_line = (View) findViewById(R.id.view_title_line);
        imageView = (ImageView) findViewById(R.id.imageView);
        tv_success = (TextView) findViewById(R.id.tv_success);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_see_details = (TextView) findViewById(R.id.tv_see_details);

        view_back.setOnClickListener(this);
    }
    private void initData() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.tv_share:

                break;
            case R.id.tv_see_details:

                break;
        }
    }
}
