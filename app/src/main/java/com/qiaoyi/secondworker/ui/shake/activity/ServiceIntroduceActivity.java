package com.qiaoyi.secondworker.ui.shake.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.VwUtils;

/**
 * Created on 2019/4/20
 *  服务介绍
 * @author Spirit
 */

public class ServiceIntroduceActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_webview);
    }
}
