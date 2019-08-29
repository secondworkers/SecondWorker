package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.SecondWorkerApplication;
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.bean.UpdateBean;
import com.qiaoyi.secondworker.bean.WrapUpdateBean;
import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.GeneralUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.UpgradeDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.isif.alibs.utils.ALibs;
import cn.isif.alibs.utils.ToastUtils;

import static com.qiaoyi.secondworker.ui.center.CenterFragment.BROADCAST_LOGOUT_ACTION;


/**
 * create on 2019/4/25
 * ling
 * 系统设置
 */
public class SystemSettingsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_safe;
    private TextView tv_update;
    private TextView tv_clear;
    private TextView tv_us;
    private Button button_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_system_settings);
        initView();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_safe:
                Intent intent = new Intent(SystemSettingsActivity.this, AccountSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_us:
                Intent intent1 = new Intent(SystemSettingsActivity.this, AboutUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_update:
                checkVersion();
                break;
            case R.id.tv_clear:
//                clearCacheDiskSelf();
//                GeneralUtils.cleanCatchDisk();
                ToastUtils.showShort("已清除" + GeneralUtils.getCacheSize());
                break;
            case R.id.button_logout:
                logout();
                break;
        }
    }
    private void logout() {
        AccountHandler.logout();
        Intent intent = new Intent();
        intent.setAction(BROADCAST_LOGOUT_ACTION);
        sendBroadcast(intent);
//        EventBus.getDefault().post(new MessageEvent("logout"));
        finish();
    }
    public static boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(ALibs.getApp()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(ALibs.getApp()).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void checkVersion() {
        ApiUserService.checkUpdate(new ServiceCallBack<WrapUpdateBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showShort("当前是最新版本");
            }

            @Override
            public void success(RespBean resp, Response<WrapUpdateBean> payload) {
                checkUpdate(payload.body().result);
            }
        });
    }

    private void checkUpdate(UpdateBean result) {
        UpgradeDialog.Builder builder = new UpgradeDialog.Builder(this);
        UpgradeDialog upgradeDialog = builder.setMessage(result.updateInfo).setOkButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDownLoadApk(result.loadUrl);
            }
        }).setCancelButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result.isMustUpdate){
                    ToastUtils.showLong("必须更新此版本，否则app无法使用！");
                    finish();
                }
            }
        }).createDialog("","",true,false);
        upgradeDialog.show();
    }

    public void goDownLoadApk(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // Verify it resolves
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(intent);
        } else {
            ToastUtils.showShort("无法找到对应的下载器");
        }
    }
    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("设置");
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_safe = (TextView) findViewById(R.id.tv_safe);
        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_us = (TextView) findViewById(R.id.tv_us);
        button_logout = (Button) findViewById(R.id.button_logout);

        view_back.setOnClickListener(this);
        tv_safe.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        tv_us.setOnClickListener(this);
        button_logout.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
