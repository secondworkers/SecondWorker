package com.qiaoyi.secondworker.ui.center.center;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.qiaoyi.secondworker.bean.MessageEvent;
import com.qiaoyi.secondworker.bean.QiNiuTokenBean;
import com.qiaoyi.secondworker.bean.UserBean;
import com.qiaoyi.secondworker.bean.WrapQiNiuTokenBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.GlideUtils;
import com.qiaoyi.secondworker.utlis.QiNiuHandler;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;

/**
 * Created on 2019/5/17
 *
 * @author Spirit
 */

public class ModifyUerInfoActivity extends BaseActivity implements View.OnClickListener , TakePhoto.TakeResultListener,
        InvokeListener {
    private TextView tv_title_txt;
    private RelativeLayout view_back;
    private TextView tv_head_photos;
    private ImageView iv_head_picture;
    private Button button_save;
    private TextView tv_name;
    private UserBean userBean;
    private String imgPath;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private String token;
    private String userName;
    private String imgURL;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VwUtils.fixScreen(this);
        setContentView(R.layout.activity_modify_userinfo);
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
        initView();
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
    }

    private void initView() {
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        view_back = (RelativeLayout) findViewById(R.id.view_back);
        tv_head_photos = (TextView) findViewById(R.id.tv_head_photos);
        iv_head_picture = (ImageView) findViewById(R.id.iv_head_picture);
        button_save = (Button) findViewById(R.id.button_save);
        tv_title_txt.setText("修改信息");
        view_back.setOnClickListener(this);
        iv_head_picture.setOnClickListener(this);
        button_save.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_name.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在修改用户信息");
        if (null!=userBean){
            refreshUI();
        }
    }
    private void refreshUI() {
        Glide.with(this).load(userBean.avatar).apply(GlideUtils.setCircleAvatar()).into(iv_head_picture);
        tv_name.setText(userBean.username);
    }
    private void showImg(String path) {
        imgPath = path;
        Glide.with(this).load(path).apply(GlideUtils.setCircleAvatar()).into(iv_head_picture);
    }

    private void upLoadToQiNiu(String imgPath) {
        new Thread(){
            @Override
            public void run() {
                String key =
                        "swAndroid/idCard/" + VwUtils.getTime("yyyyMMddHHmmss") + "_" + new java.util.Random().nextInt(
                                100000) + ".jpg";
                Configuration config = new Configuration.Builder()
                        .zone(FixedZone.zone2) // 华南地区
                        .build();
                UploadManager uploadManager = new UploadManager(config);
                uploadManager.put(imgPath, key, token, new UpCompletionHandler() {
                    @Override public void complete(String key, ResponseInfo info, JSONObject res) {
                        ALog.e("七牛 " + key + ",\r\n " + info + ",\r\n " + res);

                        if (info.isOK() && res != null && !TextUtils.isEmpty(res.optString("key"))) {
                            String imgURL = Contact.QN_IMG + res.optString("key");
                            modifyUserInfo(userBean.uid, userName,imgURL);
                        }else {
                            ToastUtils.showShort("图片上传失败");
                            dialog.dismiss();
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
    void modifyUserInfo(String id, String nickName, String avatar) {
        ApiUserService.modifyUserinfo(id, nickName, avatar, new ServiceCallBack() {
            @Override
            public void failed(String code, String errorInfo, String source) {

            }

            @Override
            public void success(RespBean resp, Response payload) {
                EventBus.getDefault().post(new MessageEvent("modify_user_info"));
                dialog.dismiss();
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                    finish();
                break;
            case R.id.iv_head_picture:
                onPictureSelect(getTakePhoto());
                break;
            case R.id.button_save:
                    submit();
                if (check()) {
                    updateUserInfo(tv_name.getText().toString(), imgPath);
                } else {
                    ToastUtils.showShort("用户信息有误，请检查！");
                }
                break;
            case R.id.tv_name:
                modifyUsername();
                break;
        }
    }
    private void modifyUsername() {
        Intent intent = new Intent(this, ModifyUsernameActivity.class);
        intent.putExtra("nickname",userBean.username);
        startActivityForResult(intent,9999);
    }
    private void updateUserInfo(String userName, String imgPath) {
        dialog.show();
        if (!TextUtils.isEmpty(imgPath)) {//修改了头像了
            //上传用户头像
            upLoadToQiNiu(imgPath);
        }else {
            modifyUserInfo(userBean.uid, userName, userBean.avatar);
        }
    }

    boolean check() {
        return !TextUtils.isEmpty(tv_name.getText().toString().trim());
    }
    private void submit() {
        // validate
        userName = tv_name.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "username", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
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
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 9999){

                String modify_nickname = data.getExtras().getString("modify_nickname");
                tv_name.setText(modify_nickname);
            }
        }
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
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto =
                    (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
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
