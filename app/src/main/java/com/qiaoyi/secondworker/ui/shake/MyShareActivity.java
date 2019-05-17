package com.qiaoyi.secondworker.ui.shake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoyi.secondworker.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 自定义分享页面
 */
public class MyShareActivity extends Activity implements View.OnClickListener {

    private Context context;
    private String webUrl;
    private String title;
    private String des;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);
        context = this;
        webUrl = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("title");
        des = getIntent().getStringExtra("des");
        img = getIntent().getStringExtra("img");

        RelativeLayout wechat = (RelativeLayout) findViewById(R.id.rl_share_wechat);
        RelativeLayout wechat_circle = (RelativeLayout) findViewById(R.id.rl_share_wechat_circle);
        RelativeLayout qq = (RelativeLayout) findViewById(R.id.rl_share_qq);
        RelativeLayout qqzone = (RelativeLayout) findViewById(R.id.rl_share_qqzone);
        RelativeLayout sina = (RelativeLayout) findViewById(R.id.rl_share_sina);
        RelativeLayout others = (RelativeLayout) findViewById(R.id.rl_others);
        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        wechat.setOnClickListener(this);
        wechat_circle.setOnClickListener(this);
        qq.setOnClickListener(this);
        qqzone.setOnClickListener(this);
        sina.setOnClickListener(this);
        others.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_share_wechat:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.rl_share_wechat_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.rl_share_qq:
                share(SHARE_MEDIA.QQ);
                break;
            case R.id.rl_share_qqzone:
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.rl_share_sina:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.rl_others:
            case R.id.tv_cancle:
                finish();
                overridePendingTransition(R.anim.push_bottom_out, 0);
                break;
        }
    }

    private void share(SHARE_MEDIA media) {
        //开启自定义分享页面
        UMWeb web = new UMWeb(webUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(des);//描述
        if (TextUtils.isEmpty(img)) {
            web.setThumb(new UMImage(this, cn.isif.umlibs.R.drawable.logo));  //本地缩略图
        } else {
            web.setThumb(new UMImage(this, img));  //网络缩略图
        }
        new ShareAction(this)
                .setPlatform(media)
                .setCallback(umShareListener)
                .withText(des)
                .withMedia(web)
                .share();
    }
    public void shareImg(SHARE_MEDIA media, Bitmap bm){
        UMImage image = new UMImage(this, R.mipmap.ic_launcher);//资源文件
        new ShareAction(this)
                .setPlatform(media)//传入平台
                // .withText("hello")//分享内容
                .withMedia(image)
                .setCallback(umShareListener)//回调监听器
                .share();
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(context, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
            finish();
            overridePendingTransition(R.anim.push_bottom_out, 0);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
            finish();
            overridePendingTransition(R.anim.push_bottom_out, 0);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.push_bottom_out, 0);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
