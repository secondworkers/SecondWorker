package com.qiaoyi.secondworker.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.BaseActivity;
import com.qiaoyi.secondworker.R;
import com.qiaoyi.secondworker.utlis.EncodingUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.InvalidParameterException;

import cn.isif.alibs.utils.ALog;
import cn.isif.alibs.utils.ToastUtils;


/**
 * Created on 2018/8/9
 *
 * @author Spirit
 */
public class ShareServiceDialog extends BaseActivity implements View.OnClickListener {
    private TextView tv_service_price;
    private TextView tv_service_name;
    private ImageView iv_qrCode;
    private TextView tv_share_wechat;
    private TextView tv_share_circle;
    private Handler mHandler = new Handler();

    private String price;
    private String serviceItem;
    private String download;
    private Bitmap bitmap;
    private RelativeLayout rl_bg,rl_bg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        serviceItem = intent.getStringExtra("serviceItem");
        download = intent.getStringExtra("download");
        initViews();
        computeWeigth();
        initData();
    }


    private void initViews() {
        setContentView(R.layout.dialog_share_service);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在处理...");
        progressDialog.setCanceledOnTouchOutside(false);
        rl_bg = findViewById(R.id.rl_bg);
        rl_bg1 = findViewById(R.id.rl_bg1);
        tv_service_price = findViewById(R.id.tv_service_price);
        tv_service_name = findViewById(R.id.tv_service_name);
        iv_qrCode = findViewById(R.id.iv_qrCode);
        tv_share_wechat = findViewById(R.id.tv_share_wechat);
        tv_share_circle = findViewById(R.id.tv_share_circle);
        tv_share_wechat.setOnClickListener(this);
        tv_share_circle.setOnClickListener(this);
        rl_bg1.setOnClickListener(this);
    }

    private void initData() {
        tv_service_price.setText(price);
        tv_service_name.setText(serviceItem);
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        iv_qrCode.setImageBitmap(
                EncodingUtils.createQRCode(download,500,500,logoBitmap));

    }

    private void computeWeigth() {
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = 0;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share_wechat:
                progressDialog.show();
                saveAndShare(SHARE_MEDIA.WEIXIN,true);
                break;
            case R.id.tv_share_circle:
                progressDialog.show();
                saveAndShare(SHARE_MEDIA.WEIXIN_CIRCLE,true);
                break;
            case R.id.rl_bg1:
                finish();
                break;
        }
    }
    @Override public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_bottom_out, 1);
    }
    private void saveAndShare(SHARE_MEDIA media,boolean isShare) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 要在运行在子线程中
                String finalImageFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sns/";
                saveScreenImage(rl_bg, finalImageFile);
                progressDialog.dismiss();
                if (isShare&&media!=null){
                    shareImg(media, bitmap);
                }else {
                    ToastUtils.showLong("图片已保存到本地");
                }
            }
        }, 100);
    }

    private void saveScreenImage(View v, String filePath) {
        try {
            bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            v.draw(canvas);
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                localFile.mkdir();
            }
            File finalImageFile = new File(localFile, System.currentTimeMillis() / 1000 + ".png");
            try {
                FileOutputStream fos = new FileOutputStream(finalImageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(finalImageFile);
                intent.setData(uri);
                sendBroadcast(intent);
                ALog.e("截图保存成功");
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                throw new InvalidParameterException();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareImg(SHARE_MEDIA media, Bitmap bm){
        UMImage imagelocal = new UMImage(this, bm);
        imagelocal.setThumb(new UMImage(this, bm));
        new ShareAction(ShareServiceDialog.this).withMedia(imagelocal )
                .setPlatform(media)
                .setCallback(shareListener).share();
    }
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ShareServiceDialog.this,"分享成功了",Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ALog.d(t.getMessage());
            Toast.makeText(ShareServiceDialog.this,"分享失败",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(ShareServiceDialog.this,"分享取消了",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
