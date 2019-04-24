package com.qiaoyi.secondworker.ui.shake.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

/**
 * Created on 2019/4/19
 *  语音识别
 * @author Spirit
 */

public class VoiceIdentifyActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
    }
}
