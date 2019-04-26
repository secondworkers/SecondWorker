package com.qiaoyi.secondworker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.local.AccountHandler;
import com.qiaoyi.secondworker.ui.MyFragmentTabHost;
import com.qiaoyi.secondworker.ui.shake.activity.BindMobileActivity;
import com.qiaoyi.secondworker.ui.center.activity.LoginActivity;
import com.qiaoyi.secondworker.ui.shake.activity.VoiceIdentifyActivity;
import com.qiaoyi.secondworker.ui.fragment.CenterFragment;
import com.qiaoyi.secondworker.ui.homepage.HomeBaseFragment;
import com.qiaoyi.secondworker.ui.map.MapFragment;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;
import com.qiaoyi.secondworker.view.ShakeDialog;

import cn.isif.alibs.utils.ToastUtils;

import static com.qiaoyi.secondworker.ui.fragment.CenterFragment.BROADCAST_LOGOUT_ACTION;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int TRICE_MESSAGE_WHAT = 0x00000000;
    private boolean mIsExit;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TRICE_MESSAGE_WHAT:
                    mIsExit = false;
                    break;

            }
        }
    };
    LogoutBroadcastReceiver mBroadcastReceiver;
    private MyFragmentTabHost mTabHost;
    private String bottomStr[] = { "首页", "", "我的" };
    private int icons[] = {
            R.drawable.selector_home_tab_home,
            R.drawable.selector_home_tab_camera,
            R.drawable.selector_home_tab_center
    };
    private LayoutInflater layoutInflater;
    public int currentIndicator;
    public static final int REQUEST_CODE = 9;
    private ImageView iv_voice;
    private TextView tv_oneKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        initView();
        new ShakeDialog(this).show();
    }

    @Override protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                ToastUtils.showShort("定位权限被拒绝无法！！！");
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_CONTACTS },
                        REQUEST_CODE);
            }
        }
//    LinkedME.getInstance().setImmediate(true);
    }
    private void initView() {
        iv_voice = findViewById(R.id.iv_voice);
        tv_oneKey = findViewById(R.id.tv_oneKey);
        iv_voice.setImageDrawable(getResources().getDrawable(R.mipmap.toolbar_voice_highlighted));
        tv_oneKey.setTextColor(getResources().getColor(R.color.text_blue));
        layoutInflater = LayoutInflater.from(this);
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content);

        mTabHost.addTab(mTabHost.newTabSpec(bottomStr[0]).setIndicator(getTabItemView(0)),
                HomeBaseFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(bottomStr[1]).setIndicator(getTabItemView(1)),
                MapFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(bottomStr[2]).setIndicator(getTabItemView(2)),
                CenterFragment.class, null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override public void onTabChanged(String tabId) {

           /*     if (bottomStr[2] == tabId) {
                    if (null == AccountHandler.checkLogin()) {
                        LoginActivity.startLoginActivity(MainActivity.this, 7001);
                        mTabHost.setCurrentTab(currentIndicator);
                    } else {
                        currentIndicator = mTabHost.getCurrentTab();
                    }
                }*/
                if (bottomStr[0] == tabId || bottomStr[2] == tabId){
                    iv_voice.setImageDrawable(getResources().getDrawable(R.mipmap.toolbar_voice));
                    tv_oneKey.setTextColor(getResources().getColor(R.color.text_grey));
                    currentIndicator = mTabHost.getCurrentTab();
                }else {
                    iv_voice.setImageDrawable(getResources().getDrawable(R.mipmap.toolbar_voice_highlighted));
                    tv_oneKey.setTextColor(getResources().getColor(R.color.text_blue));
                }
            }
        });
        mTabHost.setCurrentTab(1);
        currentIndicator = 1;
        mBroadcastReceiver = new LogoutBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_LOGOUT_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }
    private View getTabItemView(final int index) {
        View view = layoutInflater.inflate(R.layout.view_tab_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        if (index == 0)
            imageView.setImageResource(icons[index]);
        else
            imageView.setImageResource(icons[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(bottomStr[index]);
        textView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public class LogoutBroadcastReceiver extends BroadcastReceiver {

        @Override public void onReceive(Context context, Intent intent) {
            if (BROADCAST_LOGOUT_ACTION.equals(intent.getAction())) {
                mTabHost.setCurrentTab(1);
                currentIndicator = 1;
            }
        }
    }
    public void onCameraClick(View view) {

        if (currentIndicator == 1){
            ToastUtils.showShort("voice被点击了");

        }else {
            mTabHost.setCurrentTab(1);
            currentIndicator = 1;
        }
       /* if (null == AccountHandler.checkLogin()) {
            LoginActivity.startLoginActivity(MainActivity.this, 7002);
        } else {
            toRecognition();
        }*/
    }

    private void toRecognition() {
        if (AccountHandler.LOGIN_TYPE.NOBODY == AccountHandler.getUserState()) {
            //
        } else if (AccountHandler.LOGIN_TYPE.THIRD == AccountHandler.getUserState()) {
            ToastUtils.showShort("请先绑定手机号");
            BindMobileActivity.startBindMobileActivityForResult(this, AccountHandler.getUser().openId,
                    AccountHandler.getUser().nickname, AccountHandler.getUser().typeId, 7003);
            //Intent intent = new Intent(this, AccountSafeActivity.class);
            //intent.putExtra("uid", AccountHandler.checkLogin());
            //startActivityForResult(intent, 7003);
        } else if (AccountHandler.LOGIN_TYPE.MOBILE == AccountHandler.getUserState()) {
            startActivity(new Intent(this, VoiceIdentifyActivity.class));
        }
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        mTabHost = null;
        unregisterReceiver(mBroadcastReceiver);
    }
    @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
                                                     int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    ToastUtils.showShort("定位被拒绝");
                }
                return;
            }
        }
    }
    @Override
    public void onBackPressed() {
        exit();
    }
    public void exit() {
        if (!mIsExit) {
            mIsExit = true;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.trice_exit), Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(TRICE_MESSAGE_WHAT, 2000);
        } else {
            SecondWorkerApplication.getInstance().exit();
            finish();
        }
    }
}
