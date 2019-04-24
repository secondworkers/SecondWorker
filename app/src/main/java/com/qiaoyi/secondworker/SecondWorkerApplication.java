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

import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

import cn.isif.alibs.utils.ALibs;


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
