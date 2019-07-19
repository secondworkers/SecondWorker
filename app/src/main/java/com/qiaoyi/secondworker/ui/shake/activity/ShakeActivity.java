package com.qiaoyi.secondworker.ui.shake.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.WorkerBean;
import com.qiaoyi.secondworker.bean.WrapWorkerBean;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.ui.center.center.BecomeWorkerActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import cn.isif.alibs.utils.SharePreferenceUtils;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/4/20
 * 摇一摇
 *
 * @author Spirit
 */

public class ShakeActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 传感器
     */
    private SensorManager sensorManager;
    private ShakeSensorListener shakeListener;

    /**
     * 判断一次摇一摇动作
     */
    private boolean isShake = false;

    private ImageView imgHand;
    /**
     * 摇一摇动画
     */
    private ObjectAnimator anim;
    private TextView tv_goto_shake;
    private RelativeLayout view_back;
    private RelativeLayout rl_bg;
    private WorkerBean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        toStartLocation();
        setContentView(R.layout.activity_shake);
        initView();
        imgHand = findViewById(R.id.iv_shake_phone);
        tv_goto_shake = findViewById(R.id.tv_goto_shake);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeSensorListener();


        anim = ObjectAnimator.ofFloat(imgHand, "rotation", 0f, 45f, -30f, 0f);
        anim.setDuration(500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onResume() {
        //注册监听加速度传感器
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    public void onPause() {
        /**
         * 资源释放
         */
        sensorManager.unregisterListener(shakeListener);
        super.onPause();
    }

    public void onShakeClick(View view) {
        showShakeCard();
    }

    private void initView() {
        view_back = (RelativeLayout) findViewById(R.id.view_back);

        view_back.setOnClickListener(this);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        rl_bg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }

    private class ShakeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //避免一直摇
            if (isShake) {
                return;
            }
            // 开始动画
            anim.start();
            float[] values = event.values;
            /*
             * x : x轴方向的重力加速度，向右为正
             * y : y轴方向的重力加速度，向前为正
             * z : z轴方向的重力加速度，向上为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);
            //加速度超过19，摇一摇成功
            if (x > 19 || y > 19 || z > 19) {

                //播放声音
                showShakeCard();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    /**
     * 显示摇一摇结果
     */
    private void showShakeCard() {
        isShake = true;
        playSound(ShakeActivity.this);
        //震动，注意权限
        vibrate(500);
        requestData();
    }
    private int count = 1;
    private void requestData() {
        ApiUserService.shakeWorker(count, lng, lat, new ServiceCallBack<WrapWorkerBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {
                ToastUtils.showLong(errorInfo);
            }

            @Override
            public void success(RespBean resp, Response<WrapWorkerBean> payload) {
                WrapWorkerBean body = payload.body();
                result = body.result;
                if (!TextUtils.isEmpty(result.workerId)){
                    new ShakeCardDialog(ShakeActivity.this, result, new ShakeCardDialog.ShakeCardListener() {
                        @Override
                        public void refreshDialogUI(boolean shake) {
                            isShake = shake;
                        }
                    }).show();//传服务的id
                }else {
                    startActivity(new Intent(ShakeActivity.this,BecomeWorkerActivity.class));
                }

            }
        });

        SharePreferenceUtils.readInt("shake_count","shake_count",count);
        //动画取消
        anim.cancel();
    }

    private void playSound(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.shake_sound);
        player.start();
    }

    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

}
