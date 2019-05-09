package com.qiaoyi.secondworker;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qiaoyi.secondworker.utlis.DisplayUtil;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.SharePreferenceUtils;

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
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_welcome);
        mHandler = new Handler();
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        startMainActivity();
        super.onResume();
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
