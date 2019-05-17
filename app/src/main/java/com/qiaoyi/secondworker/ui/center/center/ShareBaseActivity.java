package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.ShareServiceDialog;

/**
 * Created on 2019/5/16
 *
 * @author Spirit
 */

public class ShareBaseActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_text;
    private RelativeLayout view_back;
    private TextView tv_share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_share_index);
        initView();
    }

    private void initView() {
        tv_title_text = (TextView) findViewById(R.id.tv_title_text);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_title_text.setText("分享");
        view_back.setOnClickListener(this);
        tv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.tv_share:
                Intent intent = new Intent(this, ShareServiceDialog.class);
                intent.putExtra("price","9.9元");
                intent.putExtra("serviceItem","上门送早餐服务");
                intent.putExtra("download", Contact.DOWNLOAD);
                startActivity(intent);
                overridePendingTransition(R.anim.push_bottom_in, 0);
                break;
        }
    }
}
