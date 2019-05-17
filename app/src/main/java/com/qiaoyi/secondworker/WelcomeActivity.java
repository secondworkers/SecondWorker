package com.qiaoyi.secondworker;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.utlis.VwUtils;

import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/24
 *
 * @author Spirit
 */

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    private ObjectAnimator oa = null;
    private boolean canJump = false;
    private TextView tv_goto;
    private ProgressBar progress;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_welcome);
        toStartLocation();
        mHandler = new Handler();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMainActivity();
    }
    @Override
    public void onPause() {
        canJump = false;
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        SecondWorkerApplication.getInstance().exit();
        finish();
    }

    private void initView() {
        tv_goto = (TextView) findViewById(R.id.tv_goto);
        tv_goto.setOnClickListener(this);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setOnClickListener(this);
        checkPermission();

    }

    @Override
    public void onClick(View v) {

    }

    private void startMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        },3000);
    }

}
