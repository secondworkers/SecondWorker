package com.qiaoyi.secondworker.ui.center.center;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.interfaces.INearbySearch;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.QiNiuTokenBean;
import com.qiaoyi.secondworker.bean.WrapQiNiuTokenBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.IDCardUtils;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/7
 *
 * @author Spirit
 */

public class BecomeWorker2Activity extends BaseActivity implements View.OnClickListener, TakePhoto.TakeResultListener,
        InvokeListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private EditText et_input_name;
    private EditText et_input_ID_number;
    private ImageView iv_upload_front;
    private ImageView iv_upload_reverse;
    private ImageView iv_upload_hand_front;
    private ImageView iv_upload_hand_reverse;
    private LinearLayout ll_upload_hand_photo;
    private TextView tv_next_step;
    private RelativeLayout rl_total;
    private String imgPath;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ArrayList<String> imgList;
    String token = "";
    private String img0;
    private String img1;
    private String img2;
    private String img3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_apply_become_secondworker_fill_information);
        imgList = new ArrayList<>();
        ApiUserService.getQiniuToken(new ServiceCallBack<WrapQiNiuTokenBean>() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response<WrapQiNiuTokenBean> payload) {
                QiNiuTokenBean tokenBean = payload.body().result;
                token = tokenBean.upToken;
            }
        });
        initView();
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("申请成为秒工人");
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        et_input_name = (EditText) findViewById(R.id.et_input_name);
        et_input_ID_number = (EditText) findViewById(R.id.et_input_ID_number);
        iv_upload_front = (ImageView) findViewById(R.id.iv_upload_front);
        iv_upload_reverse = (ImageView) findViewById(R.id.iv_upload_reverse);
        iv_upload_hand_front = (ImageView) findViewById(R.id.iv_upload_hand_front);
        iv_upload_hand_reverse = (ImageView) findViewById(R.id.iv_upload_hand_reverse);
        ll_upload_hand_photo = (LinearLayout) findViewById(R.id.ll_upload_hand_photo);
        tv_next_step = (TextView) findViewById(R.id.tv_next_step);
        rl_total = (RelativeLayout) findViewById(R.id.rl_total);

        view_back.setOnClickListener(this);
        iv_upload_front.setOnClickListener(this);
        iv_upload_reverse.setOnClickListener(this);
        iv_upload_hand_front.setOnClickListener(this);
        iv_upload_hand_reverse.setOnClickListener(this);
        tv_next_step.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.iv_upload_front:
                onPictureSelect(getTakePhoto());
                break;
            case R.id.iv_upload_reverse:
                onPictureSelect(getTakePhoto());
                break;
            case R.id.iv_upload_hand_front:
                onPictureSelect(getTakePhoto());
                break;
            case R.id.iv_upload_hand_reverse:
                onPictureSelect(getTakePhoto());
                break;
            case R.id.tv_next_step:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String name = et_input_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输写真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String number = et_input_ID_number.getText().toString().trim();
            if (TextUtils.isEmpty(number)) {
                Toast.makeText(this, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
                return;
            }
        if (imgList.size()==4){
            // TODO validate success, do something
            Intent intent = new Intent(this, BecomeWorker3Activity.class);
            intent.putStringArrayListExtra("imgList",imgList);
            intent.putExtra("name",name);
            intent.putExtra("number",number);
            startActivity(intent);
        }else {
            ToastUtils.showLong("请上传身份证照");
        }

    }
    private void showImg(String path) {
        imgPath = path;
        //先请求token
        if (TextUtils.isEmpty(token)) {
            token = Contact.QINIU_UPTOKEN;
        }
        upLoadToQiNiu(imgPath);

    }
    private void upLoadToQiNiu(String path) {
        new Thread(){
            @Override
            public void run() {
                String key =
                        "swAndroid/idCard/" + VwUtils.getTime("yyyyMMddHHmmss") + "_" + new java.util.Random().nextInt(
                                100000) + ".jpg";
                Configuration config = new Configuration.Builder()
                        .zone(FixedZone.zone2) // 自动识别
                        .build();
                UploadManager uploadManager = new UploadManager(config);
                uploadManager.put(path, key, token, new UpCompletionHandler() {
                    @Override public void complete(String key, ResponseInfo info, JSONObject res) {
                        ALog.e("七牛 " + key + ",\r\n " + info + ",\r\n " + res);

                        if (info.isOK() && res != null && !TextUtils.isEmpty(res.optString("key"))) {
                            imgList.add(res.optString("key"));
                            publishToServer(imgList);
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override public void progress(String key, double percent) {
                        ALog.d("七牛 " + key + ": " + percent);
                        int p = (new Double(percent * 100)).intValue();
                    }
                }, null));
            }
        }.start();
    }

    private void publishToServer(List<String> imgList) {

       if (imgList.size() == 4){
           img0 = Contact.QN_IMG+imgList.get(0);
           Glide.with(BecomeWorker2Activity.this).load(img0)
                   .apply(GlideUtils.setRoundTransform(BecomeWorker2Activity.this,5))
                   .into(iv_upload_front);
           img1 = Contact.QN_IMG+imgList.get(1);
           Glide.with(BecomeWorker2Activity.this).load(img1)
                   .apply(GlideUtils.setRoundTransform(BecomeWorker2Activity.this,5))
                   .into(iv_upload_reverse);
           img2 = Contact.QN_IMG+imgList.get(2);
           Glide.with(BecomeWorker2Activity.this).load(img2)
                   .apply(GlideUtils.setRoundTransform(BecomeWorker2Activity.this,5))
                   .into(iv_upload_hand_front);
           img3 = Contact.QN_IMG+imgList.get(3);
           Glide.with(BecomeWorker2Activity.this).load(img3)
                   .apply(GlideUtils.setRoundTransform(BecomeWorker2Activity.this,5))
                   .into(iv_upload_hand_reverse);
       }

    }

    public void onPictureSelect(TakePhoto takePhoto) {
        configCompress(takePhoto);

        File file = new File(Environment.getExternalStorageDirectory(),
                "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        takePhoto.onPickFromDocumentsWithCrop(imageUri, getCropOptions());
        //takePhoto.onPickFromDocuments();
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto =
                    (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override public void takeSuccess(TResult result) {
        try {
            ArrayList<TImage> images = result.getImages();
            if (images != null && images.size() > 0) {
                showImg(images.get(0).getOriginalPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override public void takeFail(TResult result, String msg) {

    }

    @Override public void takeCancel() {

    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type =
                PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type =
                PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 100;
        int width = 480;
        int height = 800;
        boolean showProgressBar = true;
        boolean enableRawFile = true;
        CompressConfig config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    private CropOptions getCropOptions() {
        int width = 480;
        int height = 800;
        boolean withWonCrop = false;
        CropOptions.Builder builder = new CropOptions.Builder();
        int s = 0;
        if (s == 1) {
            builder.setAspectX(width).setAspectY(height);
        } else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }
}
