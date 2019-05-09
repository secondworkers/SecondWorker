package com.qiaoyi.secondworker.ui.recognition;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.util.FileUtil;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.ui.center.center.PostRequirementActivity;
import com.qiaoyi.secondworker.utlis.AudioRecoderUtils;
import com.qiaoyi.secondworker.utlis.PopupWindowFactory;
import com.qiaoyi.secondworker.utlis.StatusBarUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.isif.alibs.utils.ALog;
import cn.isif.ifok.utils.GsonUtil;

/**
 * Created on 2019/4/19
 * 语音识别
 *
 * @author Spirit
 */

public class VoiceIdentifyActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, EventListener {
    static final int VOICE_REQUEST_CODE = 66;
    private TextView tv_skip_voice;
    private Button iv_microphone;
    private TextView tv_say;
    private ImageView iv_close;

    private ImageView mImageView;
    private TextView mTextView;
    private AudioRecoderUtils mAudioRecoderUtils;
    private PopupWindowFactory mPop;
    private RelativeLayout rl_parent;
    private String samplePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        setContentView(R.layout.activity_voice_recognition);
        initView();
        initSamplePath(this);
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr");
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法
        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    private void initView() {
        tv_skip_voice = (TextView) findViewById(R.id.tv_skip_voice);
        iv_microphone =  findViewById(R.id.iv_microphone);
        tv_say = (TextView) findViewById(R.id.tv_say);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        rl_parent = findViewById(R.id.rl_parent);

        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(this,view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_cancel);

        //6.0以上需要权限申请
        initPermission();
        iv_close.setOnClickListener(this);
        tv_skip_voice.setOnClickListener(this);
        iv_microphone.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_skip_voice:
                Intent intent = new Intent(this, PostRequirementActivity.class);
                intent.putExtra("best_result","");
                startActivity(intent);
                break;
            case R.id.iv_close:
                overridePendingTransition(0,R.anim.push_bottom_out);
                finish();
                break;
        }
    }


    int startY = 0;
    private int CANCLE_LENGTH = -200;// 上滑取消距离
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                mPop.showAtLocation(rl_parent, Gravity.CENTER, 0, 0);

                tv_say.setText("松开保存");
//                mAudioRecoderUtils.startRecord();
                start();
                break;

            case MotionEvent.ACTION_UP:
                int endY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (endY - startY < CANCLE_LENGTH) {
//                    mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                    cancelRecog();
                } else {
//                    mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                    stop();
                }
                mPop.dismiss();
                tv_say.setText("按住说话");
                break;
         /*   case MotionEvent.ACTION_MOVE:
                int tempNowY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (tempNowY - startY < CANCLE_LENGTH) {
                    tv_say.setText("手指上滑,取消发送");
                } else {
                    tv_say.setText("松开手指，取消发送");
                }
                stop();
                break;*/
            case MotionEvent.ACTION_CANCEL:
//                mAudioRecoderUtils.cancelRecord();
                cancelRecog();
                break;
        }
        return true;
    }

    private EventManager asr;
    protected boolean enableOffline = false; // 测试离线命令词，需要改成true
    /**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     * 测试参数填在这里
     */
    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
//         params.put(SpeechConstant.NLU, "enable");
         params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 3000); // 长语音
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, true); // 目前必须开启此回掉才嫩保存音频
        params.put(SpeechConstant.OUT_FILE, samplePath + "/outfile.pcm");
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
//         params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
         params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n");
                        // 可以用下面一行替代，在logcat中查看代码
                         Log.w("AutoCheckMessage", message);
                    }
                }
            }
        },enableOffline)).checkAsr(params);
        String json = null;
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        ALog.e("输入参数：" + json);
    }

    /**
     * 点击停止按钮
     *  基于SDK集成4.1 发送停止事件
     */
    private void stop() {
        ALog.e("停止识别：ASR_STOP");
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }
    private void cancelRecog() {
        ALog.e("取消识别：ASR_CANCEL");
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        Log.i("ActivityMiniRecog","On pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }
    /**
     * enableOffline设为true时，在onCreate中调用
     * 基于SDK离线命令词1.4 加载离线资源(离线时使用)
     */
    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    /**
     * enableOffline为true时，在onDestory中调用，与loadOfflineEngine对应
     * 基于SDK集成5.1 卸载离线资源步骤(离线时使用)
     */
    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }

    // 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
    // 基于SDK集成3.1 开始回调事件
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;


        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params != null && params.contains("\"final_result\"")) {
                Map<String, Object> pMap = GsonUtil.GsonToMaps(params);
                String best_result = (String) pMap.get("best_result");
                ALog.e(", 语义解析最终结果："+best_result);
                Intent intent = new Intent(this, PostRequirementActivity.class);
                intent.putExtra("best_result",best_result);
                startActivity(intent);
                finish();
            }
        }
        ALog.e(logTxt);
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
    /**
     * 创建保存OUTFILE的临时目录. 仅用于OUTFILE参数。
     *
     * @param context
     */
    public void initSamplePath(Context context) {
        String sampleDir = "SW_ASR";
        samplePath = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir;
        if (!FileUtil.makeDir(samplePath)) {
            samplePath = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            if (!FileUtil.makeDir(samplePath)) {
                throw new RuntimeException("创建临时目录失败 :" + samplePath);
            }
        }
    }

}
