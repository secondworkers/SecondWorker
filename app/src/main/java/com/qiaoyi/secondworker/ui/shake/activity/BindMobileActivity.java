package com.qiaoyi.secondworker.ui.shake.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.MainActivity;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

/**
 * Created on 2019/4/19
 *  绑定手机号
 * @author Spirit
 */

public class BindMobileActivity extends BaseActivity{
    public static void startBindMobileActivityForResult(MainActivity mainActivity, String openId, String nickname, String typeId, int i) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
    }
}
