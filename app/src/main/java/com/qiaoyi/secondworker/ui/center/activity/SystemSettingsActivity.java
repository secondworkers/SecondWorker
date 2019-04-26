package com.qiaoyi.secondworker.ui.center.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;

/**
 * create on 2019/4/25
 * ling
 * 系统设置
 */
public class SystemSettingsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        TextView textView = findViewById(R.id.tv_safe);
        TextView textView1 = findViewById(R.id.tv_us);
        textView.setOnClickListener(this);
        textView1.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_safe:
                Intent intent = new Intent(SystemSettingsActivity.this, AccountSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_us:
                Intent intent1 = new Intent(SystemSettingsActivity.this, AboutUsActivity.class);
                startActivity(intent1);
                break;
        }


    }
}
