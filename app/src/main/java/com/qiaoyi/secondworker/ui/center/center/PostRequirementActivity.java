package com.qiaoyi.secondworker.ui.center.center;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.LocationBean;
import com.qiaoyi.secondworker.ui.center.address.GetAddressActivity;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.datepicker.CustomDatePicker;
import com.qiaoyi.secondworker.view.dialog.ServiceTypeDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created on 2019/4/29
 * 发布需求
 *
 * @author Spirit
 */

public class PostRequirementActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private ImageView iv_voice;
    private TextView tv_voice_time;
    private EditText et_content;
    private RelativeLayout tv_select_photo,rl_voice_recognition;
    private TextView tv_address;
    private ImageView iv_allow_go_1;
    private TextView tv_service;
    private TextView tv_time;
    private ImageView iv_count;
    private TextView tv_number,tv_post_request;
    private ImageView iv_add;
    private EditText et_phone_number;
    private String best_result;
    private CustomDatePicker timePicker;
    private String currentTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_demand);
        VwUtils.fixScreen(this);
        initPermission();
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        rl_voice_recognition = (RelativeLayout) findViewById(R.id.rl_voice_recognition);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        tv_voice_time = (TextView) findViewById(R.id.tv_voice_time);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_select_photo = (RelativeLayout) findViewById(R.id.tv_select_photo);
        tv_address = (TextView) findViewById(R.id.tv_address);
        iv_allow_go_1 = (ImageView) findViewById(R.id.iv_allow_go_1);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_time = (TextView) findViewById(R.id.tv_time);
        iv_count = (ImageView) findViewById(R.id.iv_count);
        tv_number = (TextView) findViewById(R.id.tv_number);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        et_phone_number = (EditText) findViewById(R.id.tv_phone_number);
        tv_post_request = (TextView) findViewById(R.id.tv_post_request);

        view_back.setOnClickListener(this);
        rl_voice_recognition.setOnClickListener(this);
        tv_select_photo.setOnClickListener(this);
        tv_service.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        iv_count.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        tv_post_request.setOnClickListener(this);
        tv_address.setOnClickListener(this);
    }
    private void initData() {
        best_result = getIntent().getStringExtra("best_result");
        if(TextUtils.isEmpty(best_result)){
            rl_voice_recognition.setVisibility(View.GONE);
        }else {
            rl_voice_recognition.setVisibility(View.VISIBLE);
            et_content.setText(best_result);
        }
        et_phone_number.setText("");//用户手机号
        currentTime = VwUtils.getCurrentTime();

        //"2027-12-31 23:59"
        timePicker = new CustomDatePicker(this, "请选择服务时间", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_time.setText(time);
            }
        }, currentTime, "2250-12-31 23:59");
        timePicker.showSpecificTime(true);
        timePicker.setIsLoop(true);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onLocationSelect(LocationBean location){
        double lat = location.getLat();
        double lng = location.getLng();
        String address_msg = location.getAddress_msg();
        String address_title = location.getAddress_title();
        tv_address.setText(address_title);
        //重新根据请求数据
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.rl_voice_recognition://语音播放
                rl_voice_recognition.setClickable(false);
                try {
                    PlayShortAudioFileViaAudioTrack(Environment.getExternalStorageDirectory().getPath() + "/SW_ASR/outfile.pcm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_address:
                startActivity(new Intent(this,GetAddressActivity.class));
                break;
            case R.id.tv_service:
                    new ServiceTypeDialog(this, new ServiceTypeDialog.ServiceChooseListener() {
                        @Override
                        public void refreshDialogUI(String service_id,String service_name) {

                        }
                    }).show();
                break;
            case R.id.tv_time://选择到达时间
                timePicker.show(currentTime);
                break;
            case R.id.tv_post_request:

                break;
            case R.id.tv_select_photo://选择照片上传
                    return;

          /*  case R.id.service://选择服务类型

                break;
            case R.id.iv_count:

                break;
            case R.id.iv_add:

                break;*/

        }
    }

    private void submit() {
        // validate
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        // TODO validate success, do something
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 定义输入流，将音频写入到AudioTrack类中，实现播放
    private static final int RECORDER_SAMPLERATE = 16000;//采样率
    private static final int RECORDER_CHANNELS_OUT = AudioFormat.CHANNEL_OUT_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private void PlayShortAudioFileViaAudioTrack(String filePath) throws IOException{
        // We keep temporarily filePath globally as we have only two sample sounds now..
        if (filePath==null)
            return;
        //Reading the file..
        File file = new File(filePath); // for ex. path= "/sdcard/samplesound.pcm" or "/sdcard/samplesound.wav"
        byte[] byteData = new byte[(int) file.length()];
        Log.d("", (int) file.length()+"");
        FileInputStream in = null;
        try {
            in = new FileInputStream( file );
            in.read( byteData );
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Set and push to audio track..
        int intSize = android.media.AudioTrack.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS_OUT, RECORDER_AUDIO_ENCODING);
        Log.d("", intSize+"");
        AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS_OUT, RECORDER_AUDIO_ENCODING, intSize, AudioTrack.MODE_STREAM);
        if (at!=null) {
            at.play();
            // Write the byte array to the track
            at.write(byteData, 0, byteData.length);
            at.stop();
            at.release();
            rl_voice_recognition.setClickable(true);
        }
        else
            Log.d("", "audio track is not initialised ");
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
}
