package com.qiaoyi.secondworker;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.qiaoyi.secondworker.utlis.UmengUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

import cn.isif.alibs.utils.ALibs;
import cn.isif.alibs.utils.ALog;
import cn.isif.umlibs.UmengUtil;


public class SecondWorkerApplication extends Application {
    private static SecondWorkerApplication mApplication;
    public CopyOnWriteArrayList<Activity> mActivityList = new CopyOnWriteArrayList<Activity>();
    private String data;
    private JSONObject jsData;

    public SecondWorkerApplication(){

    }
    public static synchronized SecondWorkerApplication getInstance(){
        return mApplication;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        ALibs.init(this);//配置全局上下文访问对象
        umengInit();
    }
    void umengInit() {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel TODO:这个参数写null，不然不会去读配置的渠道！！真他妈坑！！友盟的狗篮子！！！WTF!!!
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.setLogEnabled(true);
        MobclickAgent.openActivityDurationTrack(false);
        UMConfigure.init(this, "5cbfcf584ca35779e40005ee", null, UMConfigure.DEVICE_TYPE_PHONE,
                "");
        ALog.e("device"+ UmengUtils.getDeviceInfo(this));
//        ToastUtils.showLong(UmengUtils.getDeviceInfo(this));
        //微信
//        UmengUtil.setWeixin("wx41aba1402b780e3f", "6f177c17207fbae8ff6fd0cb1521d87c");
        //新浪微博(第三个参数为回调地址)
//        UmengUtil.setSinaWeibo("278217123", "3ecf1ec67d3eb79877e6785957ce435e","http://sns.whalecloud.com/sina2/callback");
//        //QQ                                         RDQdZ9RVmfSchTeN
//        UmengUtil.setQQZone("1106795486", "RDQdZ9RVmfSchTeN");
        UmengUtil.setPlatfrom(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE);

    }
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 添加Activity到容器中
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public Activity getCurrentActivity() {
        return mActivityList.get(mActivityList.size() - 1);
    }

    /**
     * 移除容器中的activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
        }
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        System.exit(0);
    }
    /**
     * 控制debug日志输出！！！
     */
    private void switchDebugLog() {
//        ALog.allowD = BuildConfig.LOG_DEBUG;
//        ALog.allowE = BuildConfig.LOG_DEBUG;
//        ALog.allowI = BuildConfig.LOG_DEBUG;
//        ALog.allowV = BuildConfig.LOG_DEBUG;
//        ALog.allowW = BuildConfig.LOG_DEBUG;
//        ALog.allowWtf = BuildConfig.LOG_DEBUG;
    }
}
