package com.qiaoyi.secondworker.ui.center.tiktok;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.bean.QiNiuTokenBean;
import com.qiaoyi.secondworker.bean.WrapQiNiuTokenBean;
import com.qiaoyi.secondworker.net.Contact;
import com.qiaoyi.secondworker.net.RespBean;
import com.qiaoyi.secondworker.net.Response;
import com.qiaoyi.secondworker.net.ServiceCallBack;
import com.qiaoyi.secondworker.remote.ApiUserService;
import com.qiaoyi.secondworker.utlis.VwUtils;
import com.qiaoyi.secondworker.view.dialog.SubmitTaskDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created on 2019/7/7
 * 提交任务
 *
 * @author Spirit
 */

public class SubmitTaskActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener, BGASortableNinePhotoLayout.Delegate {
    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    private TextView tv_title_txt;
    private RelativeLayout view_right;
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private TextView tv_submit;
    private ArrayList<String> imgList;//保存上传七牛返回的key的list
    private ArrayList<String> pathList;//选择图片的path的list
    String token = "";
    private ProgressDialog dialog;
    private String taskAuditId,task_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        VwUtils.fixScreen(this);
        taskAuditId = getIntent().getStringExtra("taskAuditId");
        task_id = getIntent().getStringExtra("task_id");
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
        dialog = new ProgressDialog(this);
        dialog.setTitle("");
        dialog.setMessage("正在发布。。。");
        tv_title_txt = (TextView) findViewById(R.id.tv_title_txt);
        tv_title_txt.setText("任务提交");
        view_right = (RelativeLayout) findViewById(R.id.view_back);
        mPhotosSnpl = findViewById(R.id.snpl_moment_add_photos);
        mPhotosSnpl.setMaxItemCount(9);
        mPhotosSnpl.setEditable(true);//是否可编辑
        mPhotosSnpl.setPlusEnable(true);//是否显示九图控件的加号按钮
        mPhotosSnpl.setDelegate(this);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        view_right.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        for (int i = 0; i < mPhotosSnpl.getData().size(); i++) {
            Log.e("data"+i,mPhotosSnpl.getData().get(i));
        }
        // TODO validate success, do something
        imgList = new ArrayList<>();
        pathList = mPhotosSnpl.getData();
        if (pathList.size()!=0){
            //先请求token
            if (TextUtils.isEmpty(token)) {
                token = Contact.QINIU_UPTOKEN;
            }
            for (int i = 0; i < pathList.size(); i++) {
                upLoadToQiNiu(pathList.get(i));
            }
        }else {
            publishTobbs(imgList);
        }

        dialog.show();
    }
    private void upLoadToQiNiu(String path) {
        new Thread(){
            @Override
            public void run() {
                String key =
                        "snsAndroid/bbs/" + VwUtils.getTime("yyyyMMddHHmmss") + "_" + new java.util.Random().nextInt(
                                100000) + ".jpg";
//      ShowProgressDialog.showProgressOn(this,"","上传中...");
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(path, key, token, new UpCompletionHandler() {
                    @Override public void complete(String key, ResponseInfo info, JSONObject res) {
                        ALog.e("七牛 " + key + ",\r\n " + info + ",\r\n " + res);

                        if (info.isOK() && res != null && !TextUtils.isEmpty(res.optString("key"))) {
                            imgList.add(res.optString("key"));
                            if (imgList.size() == pathList.size()){
//                                if (imgList.size() == 9){
                                    publishTobbs(imgList);
//                                } else {
//                                    ToastUtils.showShort("上传数量不足");
//                                    return;
//                                }
                            }
                        } else {
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override public void progress(String key, double percent) {
                        ALog.d("七牛 " + key + ": " + percent);
                    }
                }, null));
            }
        }.start();
    }

    /**
     * 发布
     * @param img
     */
    private void publishTobbs(ArrayList<String> img) {
        StringBuilder builder = new StringBuilder();
            for (int i = 0; i < img.size(); i++) {
                if (i == (img.size() - 1)) {
                    builder.append(Contact.QN_IMG).append(img.get(i));
                } else {
                    builder.append(Contact.QN_IMG).append(img.get(i)).append(",");
                }
            }
            //TODO: post request,dialog.dismiss(),setResult(OK),
            ApiUserService.submitTask(taskAuditId, builder.toString(), new ServiceCallBack() {
                @Override
                public void failed(String code, String errorInfo, String source) {
                    ToastUtils.showShort(errorInfo);
                }

                @Override
                public void success(RespBean resp, Response payload) {
                    dialog.dismiss();
                    SubmitTaskDialog submitTaskDialog = new SubmitTaskDialog(SubmitTaskActivity.this, task_id, new SubmitTaskDialog.ServiceChooseListener() {
                        @Override
                        public void refreshDialogUI() {
                            finish();
                        }
                    });
                    submitTaskDialog.show();
                }
            });

    }
    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "sw_Download");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(mPhotosSnpl.getData()) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
         /*   if (mSingleChoiceCb.isChecked()) { //单选
                mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            } else {
            }*/
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }

}
